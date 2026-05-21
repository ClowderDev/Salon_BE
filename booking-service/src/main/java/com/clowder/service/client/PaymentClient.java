package com.clowder.service.client;

import com.clowder.dto.request.BookingDTO;
import com.clowder.dto.response.PaymentLinkResponse;
import com.clowder.enums.PaymentMethod;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("PAYMENT-SERVICE")
public interface PaymentClient {

  @PostMapping("/api/payments/create")
  public ResponseEntity<PaymentLinkResponse> createPaymentLink(
      @RequestBody BookingDTO booking,
      @RequestParam PaymentMethod paymentMethod,
      @RequestHeader("Authorization") String jwt);
}
