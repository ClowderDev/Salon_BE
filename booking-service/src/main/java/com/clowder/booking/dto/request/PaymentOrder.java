package com.clowder.booking.model;

import com.clowder.booking.enums.PaymentMethod;
import com.clowder.booking.enums.PaymentOrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
