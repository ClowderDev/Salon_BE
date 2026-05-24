package com.clowder.booking.service;

import com.clowder.booking.dto.request.NotificationDTO;
import com.clowder.booking.model.Notification;
import java.util.List;

public interface NotificationService {

  NotificationDTO createNotification(Notification notificationDTO);

  List<Notification> getNotificationsByUserId(Long userId);

  List<Notification> getAllNotificationsBySalonId(Long salonId);

  Notification markNotificationAsRead(Long notificationId);
}
