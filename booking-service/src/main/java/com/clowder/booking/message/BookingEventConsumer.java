package com.clowder.booking.message;

import com.clowder.booking.service.BookingService;
import com.clowder.common.dto.shared.PaymentOrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingEventConsumer {

  private final BookingService bookingService;

  @RabbitListener(queues = "booking-queue")
  public void bookingUpdateListener(PaymentOrderDTO paymentOrder) {
    bookingService.bookingSuccess(paymentOrder);
  }
}
