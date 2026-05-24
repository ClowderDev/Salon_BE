package com.clowder.notification.mapper;

import com.clowder.notification.dto.request.BookingDTO;
import com.clowder.notification.dto.request.NotificationDTO;
import com.clowder.notification.model.Notification;

public class NotificationMapper {

  public static NotificationDTO toDto(Notification notification, BookingDTO bookingDTO) {

    NotificationDTO notificationDTO = new NotificationDTO();
    notificationDTO.setId(notification.getId());
    notificationDTO.setType(notification.getType());
    notificationDTO.setIsRead(notification.getIsRead());
    notificationDTO.setDescription(notification.getDescription());
    notificationDTO.setBookingId(notification.getBookingId());
    notificationDTO.setUserId(notification.getUserId());
    notificationDTO.setSalonId(notification.getSalonId());
    notificationDTO.setCreatedAt(notification.getCreatedAt());

    return notificationDTO;
  }
}
