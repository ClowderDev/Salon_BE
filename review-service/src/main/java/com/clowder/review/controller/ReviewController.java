package com.clowder.review.controller;

import com.clowder.common.dto.shared.SalonDTO;
import com.clowder.common.dto.shared.UserDTO;
import com.clowder.review.dto.request.ReviewRequest;
import com.clowder.review.model.Review;
import com.clowder.review.service.ReviewService;
import com.clowder.review.service.client.SalonClient;
import com.clowder.review.service.client.UserClient;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Reviews", description = "Operations related to customer reviews")
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewService reviewService;
  private final UserClient userClient;
  private final SalonClient salonClient;

  @Operation(summary = "Create a new review for a salon")
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

  @Operation(summary = "Get all reviews for a salon")
  @GetMapping("/salon/{salonId}")
  public ResponseEntity<List<Review>> getReviewBySalonId(@PathVariable Long salonId) {

    SalonDTO salon = salonClient.getSalonById(salonId).getBody();

    List<Review> review = reviewService.getReviewBySalonId(salon.getId());

    return ResponseEntity.ok(review);
  }

  @Operation(summary = "Update a review")
  @PutMapping("/{reviewId}")
  public ResponseEntity<Review> updateReview(
      @PathVariable Long reviewId,
      @RequestBody @Valid ReviewRequest req,
      @RequestHeader("Authorization") String jwt) {

    UserDTO user = userClient.getUserProfile(jwt).getBody();
    Review review = reviewService.updateReview(req, reviewId, user.getId());

    return ResponseEntity.ok(review);
  }

  @Operation(summary = "Delete a review")
  @DeleteMapping("/{reviewId}")
  public ResponseEntity<Review> deleteReview(
      @PathVariable Long reviewId, @RequestHeader("Authorization") String jwt) {

    UserDTO user = userClient.getUserProfile(jwt).getBody();
    reviewService.deleteReview(reviewId, user.getId());

    return ResponseEntity.ok().build();
  }
}
