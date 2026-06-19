package com.clowder.category.service.impl;

import com.clowder.category.dto.request.CategoryRequest;
import com.clowder.category.model.Category;
import com.clowder.category.repository.CategoryRepository;
import com.clowder.category.service.CategoryService;
import com.clowder.common.dto.shared.SalonDTO;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;

  @Override
  @CacheEvict(value = "categories_salon", key = "#salonDTO.id")
  public Category createCategory(CategoryRequest request, SalonDTO salonDTO) {
    Category category = new Category();
    category.setName(request.getName());
    category.setSalonId(salonDTO.getId());
    return categoryRepository.save(category);
  }

  @Override
  @Cacheable(value = "categories_salon", key = "#salonId")
  public Set<Category> getAllCategoriesBySalon(Long salonId) {
    return categoryRepository.findBySalonId(salonId);
  }

  @Override
  @Cacheable(value = "category", key = "#categoryId")
  public Category getCategoryById(Long categoryId) {
    return categoryRepository
        .findById(categoryId)
        .orElseThrow(() -> new IllegalArgumentException("Category not found"));
  }

  @Override
  @Caching(evict = {
    @CacheEvict(value = "category", key = "#categoryId"),
    @CacheEvict(value = "categories_salon", key = "#salonId")
  })
  public void deleteCategory(Long categoryId, Long salonId) {
    Category category = getCategoryById(categoryId);

    if (!category.getSalonId().equals(salonId)) {
      throw new IllegalArgumentException("You don't have permission to delete this category");
    }
    categoryRepository.deleteById(categoryId);
  }

  @Override
  @Cacheable(value = "category", key = "#id")
  public Category findByIdAndSalonId(Long id, Long salonId) {
    Category category = categoryRepository.findByIdAndSalonId(id, salonId);
    if (category == null) {
      throw new IllegalArgumentException("Category not found");
    }
    return category;
  }
}
