package com.clowder.service;

import com.clowder.dto.request.NotificationDTO;
import com.clowder.model.Notification;
import java.util.List;

public interface NotificationService {

  NotificationDTO createNotification(Notification notificationDTO);

  List<Notification> getNotificationsByUserId(Long userId);

  List<Notification> getAllNotificationsBySalonId(Long salonId);

  Notification markNotificationAsRead(Long notificationId);
}
