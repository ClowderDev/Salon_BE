package com.clowder.payment.service;

import com.clowder.common.dto.shared.BookingDTO;
import com.clowder.common.dto.shared.PaymentLinkResponse;
import com.clowder.common.dto.shared.UserDTO;
import com.clowder.common.enums.PaymentMethod;
import com.clowder.payment.model.PaymentOrder;

import java.util.Map;

public interface PaymentService {

  PaymentLinkResponse createOrder(UserDTO user, BookingDTO booking, PaymentMethod paymentMethod)
      throws Exception;

  PaymentOrder getPaymentOrderById(Long id);

  PaymentOrder getPaymentOrderByPaymentId(String paymentId);

  String createVNPayPaymentUrl(UserDTO user, Long amount, Long orderId) throws Exception;

  String createMoMoPaymentUrl(UserDTO user, Long amount, Long orderId) throws Exception;

  Boolean proceedPayment(PaymentOrder paymentOrder, String paymentId, String paymentLinkId)
      throws Exception;

  /**
   * Xác minh callback từ VNPay (redirect GET params).
   * Return true nếu signature hợp lệ và giao dịch thành công.
   */
  Boolean verifyVNPayPayment(Map<String, String> params);

  /**
   * Xác minh IPN callback từ MoMo (POST body).
   * Return true nếu signature hợp lệ và resultCode == 0.
   */
  Boolean verifyMoMoPayment(Map<String, Object> ipnBody);
}
