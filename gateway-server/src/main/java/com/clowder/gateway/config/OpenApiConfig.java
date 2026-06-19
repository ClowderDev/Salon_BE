package com.clowder.gateway.config;

import java.util.List;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties.SwaggerUrl;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public SwaggerUiConfigProperties swaggerUiConfigProperties() {
        SwaggerUiConfigProperties props = new SwaggerUiConfigProperties();
        props.setUrls(List.of(
            new SwaggerUrl("booking-service",   "/booking-service/v3/api-docs",      "Booking Service"),
            new SwaggerUrl("salon-service",     "/salon-service/v3/api-docs",        "Salon Service"),
            new SwaggerUrl("user-service",      "/user-service/v3/api-docs",         "User Service"),
            new SwaggerUrl("category-service",  "/category-service/v3/api-docs",     "Category Service"),
            new SwaggerUrl("offering-service",  "/offering-service/v3/api-docs",     "Offering Service"),
            new SwaggerUrl("payment-service",   "/payment-service/v3/api-docs",      "Payment Service"),
            new SwaggerUrl("review-service",    "/review-service/v3/api-docs",       "Review Service"),
            new SwaggerUrl("notification-service", "/notification-service/v3/api-docs", "Notification Service")
        ));
        return props;
    }
}
