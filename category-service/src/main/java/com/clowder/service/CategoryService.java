package com.clowder.service;

import com.clowder.dto.request.SalonDTO;
import com.clowder.model.Category;
import java.util.Set;

public interface CategoryService {

  Category createCategory(Category category, SalonDTO salonDTO);

  Set<Category> getAllCategoriesBySalon(Long salonId);

  Category getCategoryById(Long categoryId);

  void deleteCategory(Long categoryId, Long salonId);
}
