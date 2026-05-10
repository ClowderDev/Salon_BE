package com.clowder.model;

import com.clowder.enums.PaymentMethod;
import com.clowder.enums.PaymentOrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class PaymentOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long amount;

  @Column(nullable = false)
  private PaymentOrderStatus status = PaymentOrderStatus.PENDING;

  @Column(nullable = false)
  private PaymentMethod paymentMethod;

  private String paymentLinkId;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false)
  private Long bookingId;

  @Column(nullable = false)
  private Long salonId;
}
