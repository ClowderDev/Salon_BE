package com.clowder.review.repository;

import com.clowder.review.model.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

  List<Review> findBySalonId(Long salonId);
}
