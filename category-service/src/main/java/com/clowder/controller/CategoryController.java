package com.clowder.controller;

import com.clowder.model.Category;
import com.clowder.service.CategoryService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping("/salon/{salonId}")
  public ResponseEntity<Set<Category>> getCategoriesBySalon(@PathVariable Long salonId) {
    Set<Category> categories = categoryService.getAllCategoriesBySalon(salonId);

    return ResponseEntity.ok(categories);
  }

  @GetMapping("/{categoryId}")
  public ResponseEntity<Category> getCategoryById(@PathVariable Long categoryId) {
    Category category = categoryService.getCategoryById(categoryId);

    return ResponseEntity.ok(category);
  }
}
