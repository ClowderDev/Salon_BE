package com.clowder.notification.message;

import com.clowder.notification.model.Notification;
import com.clowder.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventConsumer {

  private final NotificationService notificationService;

  @RabbitListener(queues = "notification-queue")
  public void handleNotificationEvent(Notification notification) {
    notificationService.createNotification(notification);
  }
}
