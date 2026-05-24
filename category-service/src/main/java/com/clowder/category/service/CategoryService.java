package com.clowder.booking.service;

import com.clowder.booking.dto.request.SalonDTO;
import com.clowder.booking.model.Category;
import java.util.Set;

public interface CategoryService {

  Category createCategory(Category category, SalonDTO salonDTO);

  Set<Category> getAllCategoriesBySalon(Long salonId);

  Category getCategoryById(Long categoryId);

  void deleteCategory(Long categoryId, Long salonId);

  Category findByIdAndSalonId(Long id, Long salonId);
}
