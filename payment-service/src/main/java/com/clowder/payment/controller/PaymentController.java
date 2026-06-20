package com.clowder.payment.controller;

import com.clowder.common.dto.shared.BookingDTO;
import com.clowder.common.dto.shared.PaymentLinkResponse;
import com.clowder.common.dto.shared.UserDTO;
import com.clowder.common.enums.PaymentMethod;
import com.clowder.payment.model.PaymentOrder;
import com.clowder.payment.service.PaymentService;
import com.clowder.payment.service.client.UserClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Tag(name = "Payments", description = "Operations related to payment processing and orders")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

  private final PaymentService paymentService;
  private final UserClient userClient;

  // ─── Tạo payment link ─────────────────────────────────────────────────────────

  @Operation(summary = "Tạo payment link cho booking (VNPay hoặc MoMo)")
  @PostMapping("/create")
  public ResponseEntity<PaymentLinkResponse> createPaymentLink(
      @RequestBody BookingDTO booking,
      @RequestParam PaymentMethod paymentMethod,
      @RequestHeader("Authorization") String jwt) throws Exception {

    UserDTO userDTO = userClient.getUserProfile(jwt).getBody();
    if (userDTO == null) {
      return ResponseEntity.notFound().build();
    }

    PaymentLinkResponse res = paymentService.createOrder(userDTO, booking, paymentMethod);
    return ResponseEntity.ok(res);
  }

  // ─── Query payment order ──────────────────────────────────────────────────────

  @Operation(summary = "Lấy thông tin payment order theo ID")
  @GetMapping("/{paymentOrderId}")
  public ResponseEntity<PaymentOrder> getPaymentOrderById(@PathVariable Long paymentOrderId) {
    PaymentOrder res = paymentService.getPaymentOrderById(paymentOrderId);
    return ResponseEntity.ok(res);
  }

  @Operation(summary = "Kiểm tra trạng thái payment order (frontend poll sau redirect)")
  @PatchMapping("/proceed")
  public ResponseEntity<Boolean> proceedPayment(
      @RequestParam String paymentId, @RequestParam String paymentLinkId) throws Exception {

    PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentLinkId);
    Boolean res = paymentService.proceedPayment(paymentOrder, paymentId, paymentLinkId);
    return ResponseEntity.ok(res);
  }

  // ─── VNPay callback (redirect GET từ VNPay sau khi thanh toán) ──────────────

  @Operation(summary = "VNPay return callback — VNPay redirect user về đây sau khi thanh toán")
  @GetMapping("/vnpay/callback")
  public ResponseEntity<Map<String, Object>> vnpayCallback(
      @RequestParam Map<String, String> params) {

    log.info("[VNPay] Nhận callback với params: {}", params);
    Boolean success = paymentService.verifyVNPayPayment(params);

    String orderId = params.getOrDefault("vnp_TxnRef", "");
    String responseCode = params.getOrDefault("vnp_ResponseCode", "");

    Map<String, Object> result = Map.of(
        "success", success,
        "orderId", orderId,
        "responseCode", responseCode,
        "message", success ? "Thanh toán thành công" : "Thanh toán thất bại hoặc chữ ký không hợp lệ"
    );

    return ResponseEntity.ok(result);
  }

  // ─── MoMo IPN callback (server-to-server POST từ MoMo) ──────────────────────

  @Operation(summary = "MoMo IPN webhook — MoMo gửi POST xác nhận giao dịch")
  @PostMapping("/momo/callback")
  public ResponseEntity<Map<String, Object>> momoIpnCallback(
      @RequestBody Map<String, Object> ipnBody) {

    log.info("[MoMo] Nhận IPN callback: {}", ipnBody);
    Boolean success = paymentService.verifyMoMoPayment(ipnBody);

    // MoMo yêu cầu service trả về HTTP 200 để xác nhận đã nhận IPN
    Map<String, Object> response = Map.of(
        "partnerCode", ipnBody.getOrDefault("partnerCode", ""),
        "requestId", ipnBody.getOrDefault("requestId", ""),
        "orderId", ipnBody.getOrDefault("orderId", ""),
        "resultCode", success ? 0 : 1,
        "message", success ? "Xác nhận thành công" : "Xác nhận thất bại"
    );

    return ResponseEntity.ok(response);
  }
}
