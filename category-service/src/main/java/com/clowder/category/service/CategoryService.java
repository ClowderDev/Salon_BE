package com.clowder.category.service;

import com.clowder.category.dto.request.CategoryRequest;
import com.clowder.category.model.Category;
import com.clowder.common.dto.shared.SalonDTO;
import java.util.Set;

public interface CategoryService {

  Category createCategory(CategoryRequest category, SalonDTO salonDTO);

  Set<Category> getAllCategoriesBySalon(Long salonId);

  Category getCategoryById(Long categoryId);

  void deleteCategory(Long categoryId, Long salonId);

  Category findByIdAndSalonId(Long id, Long salonId);
}
