package com.clowder.common.dto.shared;

import com.clowder.common.enums.PaymentMethod;
import com.clowder.common.enums.PaymentOrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentOrderDTO {
  private Long id;
  private Long amount;
  private PaymentOrderStatus status = PaymentOrderStatus.PENDING;
  private PaymentMethod paymentMethod;
  private String paymentLinkId;
  private Long userId;
  private Long bookingId;
  private Long salonId;
}
