package com.clowder.booking.repository;

import com.clowder.booking.model.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

  List<Review> findBySalonId(Long salonId);
}
