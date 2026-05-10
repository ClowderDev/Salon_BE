package com.clowder.controller;

import com.clowder.dto.request.BookingDTO;
import com.clowder.dto.request.UserDTO;
import com.clowder.dto.response.PaymentLinkResponse;
import com.clowder.enums.PaymentMethod;
import com.clowder.model.PaymentOrder;
import com.clowder.service.PaymentService;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

  private final PaymentService paymentService;

  @PostMapping("/create")
  public ResponseEntity<PaymentLinkResponse> createPaymentLink(
      @RequestBody BookingDTO booking, @RequestParam PaymentMethod paymentMethod)
      throws StripeException, RazorpayException {
    UserDTO user = new UserDTO();
    user.setFullName("John Doe");
    user.setEmail("jonh@gmail.com");
    user.setId(1L);

    PaymentLinkResponse res = paymentService.createOrder(user, booking, paymentMethod);
    return ResponseEntity.ok(res);
  }

  @GetMapping("/{paymentOrderId")
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
