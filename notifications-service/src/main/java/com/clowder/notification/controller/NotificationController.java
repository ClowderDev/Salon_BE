package com.clowder.notification.controller;

import com.clowder.notification.dto.request.BookingDTO;
import com.clowder.notification.dto.request.NotificationDTO;
import com.clowder.notification.mapper.NotificationMapper;
import com.clowder.notification.model.Notification;
import com.clowder.notification.service.NotificationService;
import com.clowder.notification.service.client.BookingClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

  private final NotificationService notificationService;
  private final BookingClient bookingClient;

  @PostMapping
  public ResponseEntity<NotificationDTO> createNotification(
      @RequestBody Notification notification) {
    return ResponseEntity.ok(notificationService.createNotification(notification));
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<NotificationDTO>> getNotificationsByUserId(@PathVariable Long userId) {
    List<Notification> notifications = notificationService.getNotificationsByUserId(userId);

    List<NotificationDTO> notificationDTOS =
        notifications.stream()
            .map(
                notification -> {
                  BookingDTO bookingDTO =
                      bookingClient.getBookingsById(notification.getBookingId()).getBody();
                  return NotificationMapper.toDto(notification, bookingDTO);
                })
            .toList();
    return ResponseEntity.ok(notificationDTOS);
  }

  @PutMapping("/{notificationId}/read")
  public ResponseEntity<NotificationDTO> markNotificationAsRead(@PathVariable Long notificationId) {

    Notification notification = notificationService.markNotificationAsRead(notificationId);

    BookingDTO bookingDTO = bookingClient.getBookingsById(notification.getBookingId()).getBody();
    return ResponseEntity.ok(NotificationMapper.toDto(notification, bookingDTO));
  }
}
