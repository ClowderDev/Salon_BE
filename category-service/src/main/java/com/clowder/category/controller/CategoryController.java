package com.clowder.category.controller;

import com.clowder.category.model.Category;
import com.clowder.category.service.CategoryService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Categories", description = "Public operations related to salon categories")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

  private final CategoryService categoryService;

  @Operation(summary = "Get all categories for a specific salon")
  @GetMapping("/salon/{salonId}")
  public ResponseEntity<Set<Category>> getCategoriesBySalon(@PathVariable Long salonId) {
    Set<Category> categories = categoryService.getAllCategoriesBySalon(salonId);

    return ResponseEntity.ok(categories);
  }

  @Operation(summary = "Get a category by its ID")
  @GetMapping("/{categoryId}")
  public ResponseEntity<Category> getCategoryById(@PathVariable Long categoryId) {
    Category category = categoryService.getCategoryById(categoryId);

    return ResponseEntity.ok(category);
  }
}
