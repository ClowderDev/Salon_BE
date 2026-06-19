package com.clowder.category.controller;

import com.clowder.category.dto.request.CategoryRequest;
import com.clowder.category.model.Category;
import com.clowder.category.service.CategoryService;
import com.clowder.category.service.client.SalonClient;
import com.clowder.common.dto.shared.SalonDTO;
import com.clowder.common.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Salon Owner Categories", description = "Salon owner operations related to categories")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories/salon-owner")
public class SalonCategoryController {

  private final CategoryService categoryService;
  private final SalonClient salonClient;

  @Operation(summary = "Create a category for the owner's salon")
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

  @Operation(summary = "Delete a category from the owner's salon")
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

  @Operation(summary = "Get a category by ID and Salon ID")
  @GetMapping("/salon/{salonId}/category/{id}")
  public ResponseEntity<Category> getCategoriesByIdAndSalonId(
      @PathVariable Long id, @PathVariable Long salonId) {
    Category category = categoryService.findByIdAndSalonId(id, salonId);

    return ResponseEntity.ok(category);
  }
}
