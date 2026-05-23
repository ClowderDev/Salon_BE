package com.clowder.message;

import com.clowder.dto.request.NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventProducer {
  private final RabbitTemplate rabbitTemplate;

  public void sentNotification(Long bookingId, Long userId, Long salonId) {
    NotificationDTO notificationDTO = new NotificationDTO();
    notificationDTO.setBookingId(bookingId);
    notificationDTO.setUserId(userId);
    notificationDTO.setSalonId(salonId);
    notificationDTO.setDescription("Your payment was successful for booking ID: " + bookingId);
    notificationDTO.setType("BOOKING");

    rabbitTemplate.convertAndSend("notification-event", notificationDTO);
  }
}
