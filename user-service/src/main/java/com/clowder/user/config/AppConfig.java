package com.clowder.booking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
