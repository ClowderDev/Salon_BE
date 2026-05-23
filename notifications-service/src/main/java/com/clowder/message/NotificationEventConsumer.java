package com.clowder.message;

import com.clowder.model.Notification;
import com.clowder.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class NotificationEventConsumer {

  private final NotificationService notificationService;

  @RabbitListener(queues = "notification-queue")
  public void sentNotificationEventConsumer(Notification notification) {
    notificationService.createNotification(notification);
  }
}
