package com.clowder.payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

  /**
   * RestTemplate bean dùng để gọi MoMo API.
   */
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
