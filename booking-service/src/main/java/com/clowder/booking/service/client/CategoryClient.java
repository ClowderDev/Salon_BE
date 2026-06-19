package com.clowder.booking.service.client;

import com.clowder.booking.service.client.fallback.CategoryClientFallback;
import com.clowder.common.dto.shared.CategoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "category-service", fallback = CategoryClientFallback.class)
public interface CategoryClient {

  @GetMapping("/api/categories/{categoryId}")
  public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long categoryId);

  @GetMapping("/api/categories/salon-owner/salon/{salonId}/category/{id}")
  public ResponseEntity<CategoryDTO> getCategoriesByIdAndSalonId(
      @PathVariable Long id, @PathVariable Long salonId);
}
