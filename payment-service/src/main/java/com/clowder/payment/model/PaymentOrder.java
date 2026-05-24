package com.clowder.payment.model;

import com.clowder.payment.enums.PaymentMethod;
import com.clowder.payment.enums.PaymentOrderStatus;
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
@Table(name = "payment_orders")
public class PaymentOrder extends BaseEntity {

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
