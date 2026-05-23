package com.clowder.message;

import com.clowder.model.PaymentOrder;
import com.clowder.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BookingEventConsumer {

  private final BookingService bookingService;

  @RabbitListener(queues = "booking-queue")
  public void bookingUpdateListener(PaymentOrder paymentOrder) {
    bookingService.bookingSuccess(paymentOrder);
  }
}
