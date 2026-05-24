package com.clowder.booking.service.impl;

import com.clowder.booking.dto.request.BookingDTO;
import com.clowder.booking.dto.request.NotificationDTO;
import com.clowder.booking.mapper.NotificationMapper;
import com.clowder.booking.model.Notification;
import com.clowder.booking.repository.NotificationRepository;
import com.clowder.booking.service.NotificationService;
import com.clowder.booking.service.client.BookingClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final BookingClient bookingClient;

  @Override
  public NotificationDTO createNotification(Notification notificationDTO) {

    Notification savedNotification = notificationRepository.save(notificationDTO);

    BookingDTO bookingDTO =
        bookingClient.getBookingsById(savedNotification.getBookingId()).getBody();

    NotificationDTO notificationDTO1 = NotificationMapper.toDTO(savedNotification, bookingDTO);
    return notificationDTO1;
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
