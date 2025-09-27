// src/main/java/com/macbul/platform/service/SupabaseStorageService.java
package com.macbul.platform.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class SupabaseStorageService {

  private final WebClient client;
  private final String base;   // https://<proj>.supabase.co/storage/v1
  private final String bucket;

  public SupabaseStorageService(
      @Value("${supabase.url}") String supabaseUrl,
      @Value("${supabase.serviceKey}") String serviceKey,
      @Value("${supabase.bucket}") String bucket
  ) {
    this.bucket = bucket;
    this.base = supabaseUrl.replaceAll("/+$","") + "/storage/v1";
    this.client = WebClient.builder()
        .baseUrl(this.base)
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + serviceKey)
        .defaultHeader("apikey", serviceKey)
        .build();
  }

  public void uploadDirect(String objectPath, byte[] bytes, String contentType) {
    String enc = UriUtils.encodePath(objectPath, StandardCharsets.UTF_8);
    client.post()
        .uri("/object/" + bucket + "/" + enc)
        .header("x-upsert", "true")
        .contentType(MediaType.parseMediaType(
            contentType != null ? contentType : "application/octet-stream"))
        .bodyValue(bytes)
        .retrieve()
        .onStatus(HttpStatusCode::isError, r -> r.bodyToMono(String.class)
            .flatMap(msg -> Mono.error(new IllegalStateException("Supabase direct upload failed: " + msg))))
        .toBodilessEntity()
        .timeout(Duration.ofSeconds(30))
        .block();
  }

  public String createSignedDownloadUrl(String objectPath, int expiresInSec) {
    String enc = UriUtils.encodePath(objectPath, StandardCharsets.UTF_8);
    return client.post()
        .uri("/object/sign/" + bucket + "/" + enc)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(Map.of("expiresIn", expiresInSec))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
        .map(m -> {
          Object url = m.get("url"); Object signedURL = m.get("signedURL");
          String s = url != null ? url.toString() : (signedURL != null ? signedURL.toString() : null);
          return s != null && s.startsWith("http") ? s : base + s;
        })
        .block();
  }

  public List<Map<String, Object>> listByPrefix(String prefix, int limit) {
    var body = Map.of(
        "prefix", prefix,
        "limit", limit,
        "sortBy", Map.of("column", "created_at", "order", "desc")
    );
    return client.post()
        .uri("/object/list/" + bucket)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(body)
        .retrieve()
        .onStatus(HttpStatusCode::isError, r -> r.bodyToMono(String.class)
            .flatMap(msg -> Mono.error(new IllegalStateException("Supabase list failed: " + msg))))
        .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
        .timeout(Duration.ofSeconds(15))
        .block();
  }

  public void deleteObject(String path) {
    client.delete()
        .uri("/object/" + bucket + "/" + UriUtils.encodePath(path, StandardCharsets.UTF_8))
        .retrieve()
        .onStatus(HttpStatusCode::isError, r -> r.bodyToMono(String.class)
            .flatMap(msg -> Mono.error(new IllegalStateException("Supabase delete failed: " + msg))))
        .toBodilessEntity()
        .block();
  }

  public void deleteAllUnderPrefix(String prefix) {
    String pref = prefix.endsWith("/") ? prefix : prefix + "/";
    List<Map<String, Object>> items = listByPrefix(pref, 1000);
    if (items == null) return;
    for (Map<String, Object> it : items) {
      Object name = it.get("name");
      if (name != null) deleteObject(pref + name.toString());
    }
  }
}
