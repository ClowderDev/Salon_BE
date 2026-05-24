package com.clowder.category;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.clowder.category", "com.clowder.common"})
@EnableFeignClients
public class CategoryServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(CategoryServiceApplication.class, args);
  }
}
