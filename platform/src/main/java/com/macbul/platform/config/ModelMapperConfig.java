// src/main/java/com/macbul/platform/config/ModelMapperConfig.java
package com.macbul.platform.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mm = new ModelMapper();
        mm.getConfiguration()
          .setMatchingStrategy(MatchingStrategies.STRICT)
          .setAmbiguityIgnored(true); // Ignore ambiguous mappings
        return mm;
    }
}
