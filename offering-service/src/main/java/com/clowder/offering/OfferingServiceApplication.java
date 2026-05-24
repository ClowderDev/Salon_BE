package com.clowder.offering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.clowder.offering", "com.clowder.common"})
@EnableFeignClients
public class OfferingServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(OfferingServiceApplication.class, args);
  }
}
