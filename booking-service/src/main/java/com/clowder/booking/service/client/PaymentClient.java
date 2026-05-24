package com.clowder.booking.service.client;

import com.clowder.common.dto.shared.BookingDTO;
import com.clowder.common.dto.shared.PaymentLinkResponse;
import com.clowder.common.enums.PaymentMethod;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("payment-service")
public interface PaymentClient {

  @PostMapping("/api/payments/create")
  public ResponseEntity<PaymentLinkResponse> createPaymentLink(
      @RequestBody BookingDTO booking,
      @RequestParam PaymentMethod paymentMethod,
      @RequestHeader("Authorization") String jwt);
}
