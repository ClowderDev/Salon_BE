package com.clowder.booking.service;

import com.clowder.booking.dto.request.ReviewRequest;
import com.clowder.booking.dto.request.SalonDTO;
import com.clowder.booking.dto.request.UserDTO;
import com.clowder.booking.model.Review;
import java.util.List;

public interface ReviewService {

  Review createReview(ReviewRequest req, UserDTO user, SalonDTO salon);

  List<Review> getReviewBySalonId(Long salonId);

  Review updateReview(ReviewRequest req, Long reviewId, Long userId);

  void deleteReview(Long reviewId, Long userId);
}
