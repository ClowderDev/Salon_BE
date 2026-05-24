package com.clowder.booking.mapper;

import com.clowder.booking.dto.request.BookingDTO;
import com.clowder.booking.dto.request.NotificationDTO;
import com.clowder.booking.model.Notification;

public class NotificationMapper {

  public static NotificationDTO toDTO(Notification notification, BookingDTO bookingDTO) {

    NotificationDTO notificationDTO = new NotificationDTO();
    notificationDTO.setId(notification.getId());
    notificationDTO.setType(notification.getType());
    notificationDTO.setIsRead(notification.getIsRead());
    notificationDTO.setDescription(notification.getDescription());
    notificationDTO.setBookingId(bookingDTO.getId());
    notificationDTO.setUserId(notification.getUserId());
    notificationDTO.setSalonId(notification.getSalonId());
    notificationDTO.setCreatedAt(notification.getCreatedAt());

    return notificationDTO;
  }
}
