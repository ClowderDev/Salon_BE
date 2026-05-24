package com.clowder.category.controller;

import com.clowder.category.dto.request.CategoryRequest;
import com.clowder.category.dto.request.SalonDTO;
import com.clowder.category.exception.ResourceNotFoundException;
import com.clowder.category.model.Category;
import com.clowder.category.service.CategoryService;
import com.clowder.category.service.client.SalonClient;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories/salon-owner")
public class SalonCategoryController {

  private final CategoryService categoryService;
  private final SalonClient salonClient;

  @PostMapping()
  public ResponseEntity<Category> createCategory(
      @RequestBody @Valid CategoryRequest category, @RequestHeader("Authorization") String jwt) {

    List<SalonDTO> salons = salonClient.getSalonsByOwnerId(jwt).getBody();
    if (salons == null || salons.isEmpty()) {
      throw new ResourceNotFoundException("No salon found for this owner");
    }
    SalonDTO salonDTO = salons.get(0);

    Category savedCategory = categoryService.createCategory(category, salonDTO);

    return ResponseEntity.ok(savedCategory);
  }

  @DeleteMapping("/{categoryId}")
  public ResponseEntity<Void> deleteCategory(
      @PathVariable Long categoryId, @RequestHeader("Authorization") String jwt) {

    List<SalonDTO> salons = salonClient.getSalonsByOwnerId(jwt).getBody();
    if (salons == null || salons.isEmpty()) {
      throw new ResourceNotFoundException("No salon found for this owner");
    }
    SalonDTO salonDTO = salons.get(0);
    categoryService.deleteCategory(categoryId, salonDTO.getId());
    return ResponseEntity.ok().build();
  }

  @GetMapping("/salon/{salonId}/category/{id}")
  public ResponseEntity<Category> getCategoriesByIdAndSalonId(
      @PathVariable Long id, @PathVariable Long salonId) {
    Category category = categoryService.findByIdAndSalonId(id, salonId);

    return ResponseEntity.ok(category);
  }
}
