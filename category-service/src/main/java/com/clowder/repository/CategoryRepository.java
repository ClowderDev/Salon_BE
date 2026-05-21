package com.clowder.repository;

import com.clowder.model.Category;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  Set<Category> findBySalonId(Long salonId);

  Category findByIdAndSalonId(Long id, Long salonId);
}
