package com.clowder.review.service.impl;

import com.clowder.review.dto.request.ReviewRequest;
import com.clowder.review.dto.request.SalonDTO;
import com.clowder.review.dto.request.UserDTO;
import com.clowder.review.exception.ResourceNotFoundException;
import com.clowder.review.model.Review;
import com.clowder.review.repository.ReviewRepository;
import com.clowder.review.service.ReviewService;
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
        .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
  }

  @Override
  public Review updateReview(ReviewRequest req, Long reviewId, Long userId) {

    Review review = getReviewById(reviewId);
    if (!review.getUserId().equals(userId)) {
      throw new RuntimeException("You don't have permission to modify this review");
    }

    review.setReviewText(req.getReviewText());
    review.setRating(req.getRating());

    return reviewRepository.save(review);
  }

  @Override
  public void deleteReview(Long reviewId, Long userId) {

    Review review = getReviewById(reviewId);
    if (!review.getUserId().equals(userId)) {
      throw new RuntimeException("You don't have permission to modify this review");
    }

    reviewRepository.delete(review);
  }
}
