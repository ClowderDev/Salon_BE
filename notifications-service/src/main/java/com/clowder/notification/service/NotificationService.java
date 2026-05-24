package com.clowder.notification.service;

import com.clowder.common.dto.shared.NotificationDTO;
import com.clowder.notification.model.Notification;
import java.util.List;

public interface NotificationService {

  NotificationDTO createNotification(Notification notificationDTO);

  List<Notification> getNotificationsByUserId(Long userId);

  List<Notification> getAllNotificationsBySalonId(Long salonId);

  Notification markNotificationAsRead(Long notificationId);
}
