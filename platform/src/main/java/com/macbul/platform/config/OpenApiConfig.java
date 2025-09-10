package com.macbul.platform.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI macbulOpenAPI() {
    final String schemeName = "bearerAuth";
    return new OpenAPI()
      .info(new Info().title("MacBul API").version("v1"))
      .components(new Components().addSecuritySchemes(
        schemeName,
        new SecurityScheme()
          .name(schemeName)
          .type(SecurityScheme.Type.HTTP)
          .scheme("bearer")
          .bearerFormat("JWT")
      ))
      // Tüm endpoint’lere varsayılan güvenlik (auth gerekmeyenleri SecurityConfig zaten permitAll yapıyor)
      .addSecurityItem(new SecurityRequirement().addList(schemeName));
  }
}
