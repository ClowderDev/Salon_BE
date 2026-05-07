package com.clowder.service.impl;

import com.clowder.dto.request.SalonDTO;
import com.clowder.model.Category;
import com.clowder.repository.CategoryRepository;
import com.clowder.service.CategoryService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;

  @Override
  public Category createCategory(Category category, SalonDTO salonDTO) {
    if (category.getId() != null && categoryRepository.findById(category.getId()).isPresent()) {
      throw new IllegalArgumentException("Category with the same ID already exists");
    }

    Category newCategory = new Category();
    newCategory.setName(category.getName());
    newCategory.setSalonId(category.getSalonId());
    newCategory.setImage(category.getImage());
    return categoryRepository.save(newCategory);
  }

  @Override
  public Set<Category> getAllCategoriesBySalon(Long salonId) {
    return categoryRepository.findBySalonId(salonId);
  }

  @Override
  public Category getCategoryById(Long categoryId) {
    return categoryRepository
        .findById(categoryId)
        .orElseThrow(() -> new IllegalArgumentException("Category not found"));
  }

  @Override
  public void deleteCategory(Long categoryId, Long salonId) {
    Category category = getCategoryById(categoryId);

    if (category == null) {
      throw new IllegalArgumentException("Category not found");
    }
    if (!category.getSalonId().equals(salonId)) {
      throw new IllegalArgumentException("You don't have permission to delete this category");
    }
    categoryRepository.deleteById(categoryId);
  }
}
