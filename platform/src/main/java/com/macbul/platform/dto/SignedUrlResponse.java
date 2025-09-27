package com.macbul.platform.dto;

// Supabase bazı sürümlerde "url", bazılarında "signedURL" alanını dönüyor.
// "best()" hangisi geldiyse onu verir.
public record SignedUrlResponse(String url, String signedURL, String token, String path) {
  public String best() { return url != null ? url : signedURL; }
}
