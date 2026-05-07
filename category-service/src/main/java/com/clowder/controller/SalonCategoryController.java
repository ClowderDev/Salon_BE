package com.clowder.controller;

import com.clowder.dto.request.SalonDTO;
import com.clowder.model.Category;
import com.clowder.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories/salon-owner")
public class SalonCategoryController {

  private final CategoryService categoryService;

  @PostMapping()
  public ResponseEntity<Category> createCategory(@RequestBody Category category) {

    SalonDTO salonDTO = new SalonDTO();
    salonDTO.setId(1L);

    Category savedCategory = categoryService.createCategory(category, salonDTO);

    return ResponseEntity.ok(savedCategory);
  }

  @DeleteMapping("/{categoryId}")
  public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {

    SalonDTO salonDTO = new SalonDTO();
    salonDTO.setId(1L);
    categoryService.deleteCategory(categoryId, salonDTO.getId());
    return ResponseEntity.ok().build();
  }
}
