package com.clowder.review.controller;

import com.clowder.review.dto.request.ReviewRequest;
import com.clowder.review.dto.request.SalonDTO;
import com.clowder.review.dto.request.UserDTO;
import com.clowder.review.model.Review;
import com.clowder.review.service.ReviewService;
import com.clowder.review.service.client.SalonClient;
import com.clowder.review.service.client.UserClient;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewService reviewService;
  private final UserClient userClient;
  private final SalonClient salonClient;

  @PostMapping("/salon/{salonId}")
  public ResponseEntity<Review> createReview(
      @PathVariable Long salonId,
      @RequestBody @Valid ReviewRequest req,
      @RequestHeader("Authorization") String jwt) {

    UserDTO user = userClient.getUserProfile(jwt).getBody();
    SalonDTO salon = salonClient.getSalonById(salonId).getBody();

    Review review = reviewService.createReview(req, user, salon);

    return ResponseEntity.ok(review);
  }

  @GetMapping("/salon/{salonId}")
  public ResponseEntity<List<Review>> getReviewBySalonId(@PathVariable Long salonId) {

    SalonDTO salon = salonClient.getSalonById(salonId).getBody();

    List<Review> review = reviewService.getReviewBySalonId(salon.getId());

    return ResponseEntity.ok(review);
  }

  @PutMapping("/{reviewId}")
  public ResponseEntity<Review> updateReview(
      @PathVariable Long reviewId,
      @RequestBody @Valid ReviewRequest req,
      @RequestHeader("Authorization") String jwt) {

    UserDTO user = userClient.getUserProfile(jwt).getBody();
    Review review = reviewService.updateReview(req, reviewId, user.getId());

    return ResponseEntity.ok(review);
  }

  @DeleteMapping("/{reviewId}")
  public ResponseEntity<Review> deleteReview(
      @PathVariable Long reviewId, @RequestHeader("Authorization") String jwt) {

    UserDTO user = userClient.getUserProfile(jwt).getBody();
    reviewService.deleteReview(reviewId, user.getId());

    return ResponseEntity.ok().build();
  }
}
