package com.clowder.review.model;

import com.clowder.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reviews")
public class Review extends BaseEntity {

  @Column(nullable = false)
  private String reviewText;

  @Column(nullable = false)
  private double rating;

  @Column(nullable = false)
  private Long salonId;

  @Column(nullable = false)
  private Long userId;
}
