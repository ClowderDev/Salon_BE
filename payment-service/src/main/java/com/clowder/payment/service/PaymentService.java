package com.clowder.payment.service;

import com.clowder.common.dto.shared.BookingDTO;
import com.clowder.common.dto.shared.PaymentLinkResponse;
import com.clowder.common.dto.shared.UserDTO;
import com.clowder.common.enums.PaymentMethod;
import com.clowder.payment.model.PaymentOrder;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

public interface PaymentService {

  PaymentLinkResponse createOrder(UserDTO user, BookingDTO booking, PaymentMethod paymentMethod)
      throws RazorpayException, StripeException;

  PaymentOrder getPaymentOrderById(Long id);

  PaymentOrder getPaymentOrderByPaymentId(String paymentId);

  PaymentLink createRazorPaymentLink(UserDTO user, Long amount, Long orderId)
      throws RazorpayException;

  String createStripePaymentLink(UserDTO user, Long amount, Long orderId) throws StripeException;

  Boolean proceedPayment(PaymentOrder paymentOrder, String paymentId, String paymentLinkId)
      throws RazorpayException;
}
