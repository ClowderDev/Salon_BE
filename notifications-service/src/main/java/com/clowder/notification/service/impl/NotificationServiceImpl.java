package com.clowder.notification.service.impl;

import com.clowder.notification.dto.request.BookingDTO;
import com.clowder.notification.dto.request.NotificationDTO;
import com.clowder.notification.mapper.NotificationMapper;
import com.clowder.notification.model.Notification;
import com.clowder.notification.repository.NotificationRepository;
import com.clowder.notification.service.NotificationService;
import com.clowder.notification.service.client.BookingClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final BookingClient bookingClient;

  @Override
  public NotificationDTO createNotification(Notification notification) {

    Notification savedNotification = notificationRepository.save(notification);
    BookingDTO bookingDTO =
        bookingClient.getBookingsById(savedNotification.getBookingId()).getBody();
    return NotificationMapper.toDto(savedNotification, bookingDTO);
  }

  @Override
  public List<Notification> getNotificationsByUserId(Long userId) {
    return notificationRepository.findByUserId(userId);
  }

  @Override
  public List<Notification> getAllNotificationsBySalonId(Long salonId) {
    return notificationRepository.findBySalonId(salonId);
  }

  @Override
  public Notification markNotificationAsRead(Long notificationId) {
    return notificationRepository
        .findById(notificationId)
        .map(
            notification -> {
              notification.setIsRead(true);
              return notificationRepository.save(notification);
            })
        .orElseThrow(
            () -> new RuntimeException("Notification not found with id: " + notificationId));
  }
}
