package com.clowder.payment.service.impl;

import com.clowder.payment.dto.request.BookingDTO;
import com.clowder.payment.dto.request.UserDTO;
import com.clowder.payment.dto.response.PaymentLinkResponse;
import com.clowder.payment.enums.PaymentMethod;
import com.clowder.payment.enums.PaymentOrderStatus;
import com.clowder.payment.message.BookingEventProducer;
import com.clowder.payment.message.NotificationEventProducer;
import com.clowder.payment.model.PaymentOrder;
import com.clowder.payment.repository.PaymentRepository;
import com.clowder.payment.service.PaymentService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.Mode;
import com.stripe.param.checkout.SessionCreateParams.PaymentMethodType;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

  private final PaymentRepository paymentRepository;
  private final BookingEventProducer bookingEventProducer;
  private final NotificationEventProducer notificationEventProducer;

  @Value("${stripe.api.key}")
  private String stripeSecretKey;

  @Value("${razorpay.api.key}")
  private String razorpayApiKey;

  @Value("${razorpay.api.secret}")
  private String razorpayApiSecret;

  @Override
  public PaymentLinkResponse createOrder(
      UserDTO user, BookingDTO booking, PaymentMethod paymentMethod)
      throws RazorpayException, StripeException {

    Long amount = (long) booking.getTotalPrice();

    PaymentOrder paymentOrder = new PaymentOrder();
    paymentOrder.setAmount(amount);
    paymentOrder.setPaymentMethod(paymentMethod);
    paymentOrder.setSalonId(booking.getSalonId());
    paymentOrder.setBookingId(booking.getId());

    PaymentOrder savedOrder = paymentRepository.save(paymentOrder);

    PaymentLinkResponse paymentLinkResponse = new PaymentLinkResponse();

    if (paymentMethod.equals(PaymentMethod.RAZORPAY)) {
      PaymentLink payment =
          createRazorPaymentLink(user, savedOrder.getAmount(), savedOrder.getId());

      String paymentUrl = payment.get("short_url");
      String paymentUrlId = payment.get("id");

      paymentLinkResponse.setPaymentLinkUrl(paymentUrl);
      paymentLinkResponse.setPaymentLinkId(paymentUrlId);
      savedOrder.setPaymentLinkId(paymentUrlId);

      paymentRepository.save(savedOrder);
    } else {
      String paymentUrl = createStripePaymentLink(user, savedOrder.getAmount(), savedOrder.getId());
      paymentLinkResponse.setPaymentLinkUrl(paymentUrl);
    }
    return paymentLinkResponse;
  }

  @Override
  public PaymentOrder getPaymentOrderById(Long id) {
    return paymentRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Payment not found"));
  }

  @Override
  public PaymentOrder getPaymentOrderByPaymentId(String paymentId) {
    return paymentRepository.findByPaymentLinkId(paymentId);
  }

  @Override
  public PaymentLink createRazorPaymentLink(UserDTO user, Long amount, Long orderId)
      throws RazorpayException {

    Long amountInPaise = amount * 100;

    RazorpayClient razorpayClient = new RazorpayClient(razorpayApiKey, razorpayApiSecret);

    JSONObject paymentLinkRequest = new JSONObject();
    paymentLinkRequest.put("amount", amountInPaise);
    paymentLinkRequest.put("currency", "VND");

    JSONObject customer = new JSONObject();
    customer.put("name", user.getFullName());
    customer.put("email", user.getEmail());

    paymentLinkRequest.put("customer", customer);

    JSONObject notify = new JSONObject();
    notify.put("email", true);

    paymentLinkRequest.put("notify", notify);

    paymentLinkRequest.put("reminder_enable", true);

    paymentLinkRequest.put("calback_url", "http:localhost:3000/payment-success/" + orderId);

    paymentLinkRequest.put("callback_method", "get");

    return razorpayClient.paymentLink.create(paymentLinkRequest);
  }

  @Override
  public String createStripePaymentLink(UserDTO user, Long amount, Long orderId)
      throws StripeException {

    Stripe.apiKey = stripeSecretKey;

    SessionCreateParams params =
        SessionCreateParams.builder()
            .addPaymentMethodType(PaymentMethodType.CARD)
            .setMode(Mode.PAYMENT)
            .setSuccessUrl("http://localhost:3000/payment-success/" + orderId)
            .setCancelUrl("http://localhost:3000/payment/cancel")
            .addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setQuantity(1L)
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("vnd")
                            .setUnitAmount(amount * 100)
                            .setProductData(
                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName("Booking Payment")
                                    .build())
                            .build())
                    .build())
            .build();

    Session session = Session.create(params);

    return session.getUrl();
  }

  @Override
  public Boolean proceedPayment(PaymentOrder paymentOrder, String paymentId, String paymentLinkId)
      throws RazorpayException {
    if (paymentOrder.getStatus().equals(PaymentMethod.RAZORPAY)) {
      RazorpayClient razorpay = new RazorpayClient(razorpayApiKey, razorpayApiSecret);

      Payment payment = razorpay.payments.fetch(paymentId);
      Integer amount = payment.get("amount");
      String status = payment.get("status");

      if (status.equals("captured")) {

        bookingEventProducer.sendBookingUpdateEvent(paymentOrder);
        notificationEventProducer.sendNotification(
            paymentOrder.getBookingId(), paymentOrder.getUserId(), paymentOrder.getSalonId());
        paymentOrder.setStatus(PaymentOrderStatus.SUCCEEDED);
        paymentRepository.save(paymentOrder);
        return true;
      }
      return false;
    } else {
      paymentOrder.setStatus(PaymentOrderStatus.SUCCEEDED);
      paymentRepository.save(paymentOrder);
      return true;
    }
  }
}
