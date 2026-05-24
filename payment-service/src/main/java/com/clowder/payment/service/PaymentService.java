package com.clowder.booking.service;

import com.clowder.booking.dto.request.BookingDTO;
import com.clowder.booking.dto.request.UserDTO;
import com.clowder.booking.dto.response.PaymentLinkResponse;
import com.clowder.booking.enums.PaymentMethod;
import com.clowder.booking.model.PaymentOrder;
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
