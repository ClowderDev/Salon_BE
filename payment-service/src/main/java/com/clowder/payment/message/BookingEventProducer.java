package com.clowder.payment.message;

import com.clowder.payment.model.PaymentOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingEventProducer {
  private final RabbitTemplate rabbitTemplate;

  public void sendBookingUpdateEvent(PaymentOrder paymentOrder) {
    rabbitTemplate.convertAndSend("booking-queue", paymentOrder);
  }
}
