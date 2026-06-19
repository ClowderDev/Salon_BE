package com.clowder.salon.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI salonServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Salon Service API")
                        .description("Manages salon profiles, search, and owner information")
                        .version("1.0.0"));
    }
}
