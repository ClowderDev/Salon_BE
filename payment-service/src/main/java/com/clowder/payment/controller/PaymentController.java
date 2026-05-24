package com.clowder.payment.controller;

import com.clowder.common.dto.shared.BookingDTO;
import com.clowder.common.dto.shared.PaymentLinkResponse;
import com.clowder.common.dto.shared.UserDTO;
import com.clowder.common.enums.PaymentMethod;
import com.clowder.payment.model.PaymentOrder;
import com.clowder.payment.service.PaymentService;
import com.clowder.payment.service.client.UserClient;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

  private final PaymentService paymentService;
  private final UserClient userClient;

  @PostMapping("/create")
  public ResponseEntity<PaymentLinkResponse> createPaymentLink(
      @RequestBody BookingDTO booking,
      @RequestParam PaymentMethod paymentMethod,
      @RequestHeader("Authorization") String jwt)
      throws StripeException, RazorpayException {
    UserDTO userDTO = userClient.getUserProfile(jwt).getBody();

    if (userDTO == null) {
      return ResponseEntity.notFound().build();
    }

    PaymentLinkResponse res = paymentService.createOrder(userDTO, booking, paymentMethod);
    return ResponseEntity.ok(res);
  }

  @GetMapping("/{paymentOrderId}")
  public ResponseEntity<PaymentOrder> getPaymentOrderById(@PathVariable Long paymentOrderId) {

    PaymentOrder res = paymentService.getPaymentOrderById(paymentOrderId);
    return ResponseEntity.ok(res);
  }

  @PatchMapping("/proceed")
  public ResponseEntity<Boolean> proceedPayment(
      @RequestParam String paymentId, @RequestParam String paymentLinkId) throws Exception {

    PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentLinkId);

    Boolean res = paymentService.proceedPayment(paymentOrder, paymentId, paymentLinkId);
    return ResponseEntity.ok(res);
  }
}
