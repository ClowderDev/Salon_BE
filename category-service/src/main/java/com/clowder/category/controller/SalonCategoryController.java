package com.clowder.booking.controller;

import com.clowder.booking.dto.request.SalonDTO;
import com.clowder.booking.model.Category;
import com.clowder.booking.service.CategoryService;
import com.clowder.booking.service.client.SalonClient;
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
      @RequestBody Category category, @RequestHeader("Authorization") String jwt) {

    SalonDTO salonDTO = (SalonDTO) salonClient.getSalonsByOwnerId(jwt).getBody();

    Category savedCategory = categoryService.createCategory(category, salonDTO);

    return ResponseEntity.ok(savedCategory);
  }

  @DeleteMapping("/{categoryId}")
  public ResponseEntity<Void> deleteCategory(
      @PathVariable Long categoryId, @RequestHeader("Authorization") String jwt) {

    SalonDTO salonDTO = (SalonDTO) salonClient.getSalonsByOwnerId(jwt).getBody();
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
