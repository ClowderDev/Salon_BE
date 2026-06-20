package com.clowder.payment.service.impl;

import com.clowder.common.dto.shared.BookingDTO;
import com.clowder.common.dto.shared.PaymentLinkResponse;
import com.clowder.common.dto.shared.UserDTO;
import com.clowder.common.enums.PaymentMethod;
import com.clowder.common.enums.PaymentOrderStatus;
import com.clowder.payment.message.BookingEventProducer;
import com.clowder.payment.message.NotificationEventProducer;
import com.clowder.payment.model.PaymentOrder;
import com.clowder.payment.repository.PaymentRepository;
import com.clowder.payment.service.PaymentService;
import com.clowder.payment.util.MoMoUtils;
import com.clowder.payment.util.VNPayUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@RefreshScope
public class PaymentServiceImpl implements PaymentService {

  private final PaymentRepository paymentRepository;
  private final BookingEventProducer bookingEventProducer;
  private final NotificationEventProducer notificationEventProducer;
  private final RestTemplate restTemplate;

  // ─── VNPay config ────────────────────────────────────────────────────────────
  @Value("${vnpay.tmn-code}")
  private String vnpTmnCode;

  @Value("${vnpay.hash-secret}")
  private String vnpHashSecret;

  @Value("${vnpay.payment-url}")
  private String vnpPaymentUrl;

  @Value("${vnpay.return-url}")
  private String vnpReturnUrl;

  // ─── MoMo config ─────────────────────────────────────────────────────────────
  @Value("${momo.partner-code}")
  private String momoPartnerCode;

  @Value("${momo.access-key}")
  private String momoAccessKey;

  @Value("${momo.secret-key}")
  private String momoSecretKey;

  @Value("${momo.endpoint}")
  private String momoEndpoint;

  @Value("${momo.return-url}")
  private String momoReturnUrl;

  @Value("${momo.notify-url}")
  private String momoNotifyUrl;

  // ─────────────────────────────────────────────────────────────────────────────

  @Override
  public PaymentLinkResponse createOrder(
      UserDTO user, BookingDTO booking, PaymentMethod paymentMethod) throws Exception {

    Long amount = (long) booking.getTotalPrice();

    PaymentOrder paymentOrder = new PaymentOrder();
    paymentOrder.setAmount(amount);
    paymentOrder.setPaymentMethod(paymentMethod);
    paymentOrder.setSalonId(booking.getSalonId());
    paymentOrder.setBookingId(booking.getId());
    paymentOrder.setUserId(user.getId());

    PaymentOrder savedOrder = paymentRepository.save(paymentOrder);

    PaymentLinkResponse response = new PaymentLinkResponse();
    String paymentUrl;

    if (paymentMethod == PaymentMethod.VNPAY) {
      paymentUrl = createVNPayPaymentUrl(user, savedOrder.getAmount(), savedOrder.getId());
      // VNPay dùng orderId làm txnRef, không có linkId riêng
      response.setPaymentLinkId(String.valueOf(savedOrder.getId()));
    } else {
      // MOMO
      paymentUrl = createMoMoPaymentUrl(user, savedOrder.getAmount(), savedOrder.getId());
      response.setPaymentLinkId(String.valueOf(savedOrder.getId()));
    }

    response.setPaymentLinkUrl(paymentUrl);
    return response;
  }

  // ─── VNPay ───────────────────────────────────────────────────────────────────

  @Override
  public String createVNPayPaymentUrl(UserDTO user, Long amount, Long orderId) throws Exception {
    String vnpTxnRef = String.valueOf(orderId);
    String vnpCreateDate = VNPayUtils.getCurrentDateTime();
    String vnpExpireDate = VNPayUtils.getExpireDateTime(15);

    // VNPay yêu cầu số tiền * 100 (đơn vị: đồng → xu)
    // VND không có xu trong thực tế nhưng VNPay vẫn yêu cầu nhân 100
    String vnpAmount = String.valueOf(amount * 100);

    String returnUrlWithOrder = vnpReturnUrl + "/" + orderId;

    Map<String, String> params = new LinkedHashMap<>();
    params.put("vnp_Version", "2.1.0");
    params.put("vnp_Command", "pay");
    params.put("vnp_TmnCode", vnpTmnCode);
    params.put("vnp_Amount", vnpAmount);
    params.put("vnp_CurrCode", "VND");
    params.put("vnp_BankCode", "");          // để trống → user chọn ngân hàng tại VNPay
    params.put("vnp_TxnRef", vnpTxnRef);
    params.put("vnp_OrderInfo", "Thanh toan booking #" + orderId);
    params.put("vnp_OrderType", "other");
    params.put("vnp_Locale", "vn");
    params.put("vnp_ReturnUrl", returnUrlWithOrder);
    params.put("vnp_IpAddr", "127.0.0.1");
    params.put("vnp_CreateDate", vnpCreateDate);
    params.put("vnp_ExpireDate", vnpExpireDate);

    String queryString = VNPayUtils.buildQueryString(vnpHashSecret, params);
    String paymentUrl = vnpPaymentUrl + "?" + queryString;

    log.info("[VNPay] Tạo payment URL cho orderId={}, amount={}", orderId, amount);
    return paymentUrl;
  }

  @Override
  public Boolean verifyVNPayPayment(Map<String, String> params) {
    String responseCode = params.get("vnp_ResponseCode");
    String transactionStatus = params.get("vnp_TransactionStatus");

    // Xác minh signature
    boolean validSignature = VNPayUtils.verifySignature(vnpHashSecret, params);
    if (!validSignature) {
      log.warn("[VNPay] Signature không hợp lệ! Params: {}", params);
      return false;
    }

    // "00" = giao dịch thành công
    boolean success = "00".equals(responseCode) && "00".equals(transactionStatus);
    if (success) {
      String txnRef = params.get("vnp_TxnRef");
      Long orderId = Long.parseLong(txnRef);
      PaymentOrder paymentOrder = paymentRepository.findById(orderId)
          .orElse(null);

      if (paymentOrder != null && paymentOrder.getStatus() == PaymentOrderStatus.PENDING) {
        paymentOrder.setStatus(PaymentOrderStatus.SUCCEEDED);
        paymentRepository.save(paymentOrder);
        bookingEventProducer.sendBookingUpdateEvent(paymentOrder);
        notificationEventProducer.sendNotification(
            paymentOrder.getBookingId(),
            paymentOrder.getUserId(),
            paymentOrder.getSalonId());
        log.info("[VNPay] Xác nhận thanh toán thành công orderId={}", orderId);
      }
    } else {
      log.info("[VNPay] Giao dịch thất bại, responseCode={}", responseCode);
    }
    return success;
  }

  // ─── MoMo ────────────────────────────────────────────────────────────────────

  @Override
  public String createMoMoPaymentUrl(UserDTO user, Long amount, Long orderId) throws Exception {
    String requestId = momoPartnerCode + System.currentTimeMillis();
    String orderIdStr = String.valueOf(orderId);
    String orderInfo = "Thanh toan booking #" + orderId;
    String extraData = "";
    String requestType = "payWithMethod"; // sandbox: captureWallet hoặc payWithMethod
    String returnUrlWithOrder = momoReturnUrl + "/" + orderId;

    // Tạo raw hash và signature
    String rawHash = MoMoUtils.buildRawHash(
        momoAccessKey, String.valueOf(amount), extraData, momoNotifyUrl,
        orderIdStr, orderInfo, momoPartnerCode, returnUrlWithOrder, requestId, requestType);
    String signature = MoMoUtils.hmacSha256(momoSecretKey, rawHash);

    // Build request body
    Map<String, Object> requestBody = new LinkedHashMap<>();
    requestBody.put("partnerCode", momoPartnerCode);
    requestBody.put("accessKey", momoAccessKey);
    requestBody.put("requestId", requestId);
    requestBody.put("amount", String.valueOf(amount));
    requestBody.put("orderId", orderIdStr);
    requestBody.put("orderInfo", orderInfo);
    requestBody.put("redirectUrl", returnUrlWithOrder);
    requestBody.put("ipnUrl", momoNotifyUrl);
    requestBody.put("extraData", extraData);
    requestBody.put("requestType", requestType);
    requestBody.put("signature", signature);
    requestBody.put("lang", "vi");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

    log.info("[MoMo] Gửi request tạo payment cho orderId={}, amount={}", orderId, amount);

    ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
        momoEndpoint, HttpMethod.POST, entity, (Class<Map<String, Object>>) (Class<?>) Map.class);

    Map<String, Object> body = response.getBody();
    if (body == null) {
      throw new RuntimeException("MoMo trả về response rỗng");
    }

    Integer resultCode = (Integer) body.get("resultCode");
    if (resultCode == null || resultCode != 0) {
      String message = (String) body.getOrDefault("message", "Unknown error");
      log.error("[MoMo] Tạo payment thất bại: resultCode={}, message={}", resultCode, message);
      throw new RuntimeException("MoMo tạo payment thất bại: " + message);
    }

    String payUrl = (String) body.get("payUrl");
    log.info("[MoMo] Tạo payment thành công, payUrl={}", payUrl);
    return payUrl;
  }

  @Override
  public Boolean verifyMoMoPayment(Map<String, Object> ipnBody) {
    try {
      Integer resultCode = (Integer) ipnBody.get("resultCode");
      String orderId = (String) ipnBody.get("orderId");
      String receivedSig = (String) ipnBody.get("signature");

      // Re-build raw hash để verify signature
      String rawHash = MoMoUtils.buildRawHash(
          momoAccessKey,
          String.valueOf(ipnBody.getOrDefault("amount", "")),
          String.valueOf(ipnBody.getOrDefault("extraData", "")),
          momoNotifyUrl,
          orderId,
          String.valueOf(ipnBody.getOrDefault("orderInfo", "")),
          momoPartnerCode,
          momoReturnUrl + "/" + orderId,
          String.valueOf(ipnBody.getOrDefault("requestId", "")),
          String.valueOf(ipnBody.getOrDefault("requestType", "")));

      String calculatedSig = MoMoUtils.hmacSha256(momoSecretKey, rawHash);
      if (!calculatedSig.equals(receivedSig)) {
        log.warn("[MoMo] IPN signature không hợp lệ! orderId={}", orderId);
        return false;
      }

      if (resultCode != null && resultCode == 0) {
        Long orderIdLong = Long.parseLong(orderId);
        PaymentOrder paymentOrder = paymentRepository.findById(orderIdLong).orElse(null);

        if (paymentOrder != null && paymentOrder.getStatus() == PaymentOrderStatus.PENDING) {
          paymentOrder.setStatus(PaymentOrderStatus.SUCCEEDED);
          paymentRepository.save(paymentOrder);
          bookingEventProducer.sendBookingUpdateEvent(paymentOrder);
          notificationEventProducer.sendNotification(
              paymentOrder.getBookingId(),
              paymentOrder.getUserId(),
              paymentOrder.getSalonId());
          log.info("[MoMo] IPN xác nhận thanh toán thành công orderId={}", orderIdLong);
        }
        return true;
      }

      log.info("[MoMo] IPN giao dịch thất bại, resultCode={}", resultCode);
      return false;
    } catch (Exception e) {
      log.error("[MoMo] Lỗi xử lý IPN callback: {}", e.getMessage(), e);
      return false;
    }
  }

  // ─── Common ──────────────────────────────────────────────────────────────────

  @Override
  public PaymentOrder getPaymentOrderById(Long id) {
    return paymentRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Payment not found: " + id));
  }

  @Override
  public PaymentOrder getPaymentOrderByPaymentId(String paymentId) {
    return paymentRepository.findByPaymentLinkId(paymentId);
  }

  @Override
  public Boolean proceedPayment(PaymentOrder paymentOrder, String paymentId, String paymentLinkId)
      throws Exception {
    // proceedPayment được dùng khi frontend poll sau redirect.
    // Với VNPay: verify đã xảy ra ở /vnpay/callback nên chỉ cần check DB status.
    // Với MoMo: verify đã xảy ra ở /momo/callback (IPN), cũng chỉ cần check DB.
    if (paymentOrder.getStatus() == PaymentOrderStatus.SUCCEEDED) {
      return true;
    }
    log.warn("[proceedPayment] PaymentOrder {} chưa được xác nhận (status={})",
        paymentOrder.getId(), paymentOrder.getStatus());
    return false;
  }
}
