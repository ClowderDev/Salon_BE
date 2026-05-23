package com.clowder.service.impl;

import com.clowder.dto.request.ReviewRequest;
import com.clowder.dto.request.SalonDTO;
import com.clowder.dto.request.UserDTO;
import com.clowder.model.Review;
import com.clowder.repository.ReviewRepository;
import com.clowder.service.ReviewService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

  private final ReviewRepository reviewRepository;

  @Override
  public Review createReview(ReviewRequest req, UserDTO user, SalonDTO salon) {

    Review review = new Review();
    review.setReviewText(req.getReviewText());
    review.setRating(req.getRating());
    review.setUserId(user.getId());
    review.setSalonId(salon.getId());
    return reviewRepository.save(review);
  }

  @Override
  public List<Review> getReviewBySalonId(Long salonId) {
    return reviewRepository.findBySalonId(salonId);
  }

  private Review getReviewById(Long id) {
    return reviewRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Review not found"));
  }

  @Override
  public Review updateReview(ReviewRequest req, Long reviewId, Long userId) {

    Review review = getReviewById(reviewId);
    if (!review.getUserId().equals(userId)) {
      throw new RuntimeException("Review not found");
    }

    review.setReviewText(req.getReviewText());
    review.setRating(req.getRating());

    return reviewRepository.save(review);
  }

  @Override
  public void deleteReview(Long reviewId, Long userId) {

    Review review = getReviewById(reviewId);
    if (!review.getUserId().equals(userId)) {
      throw new RuntimeException("Review not found");
    }

    reviewRepository.delete(review);
  }
}
