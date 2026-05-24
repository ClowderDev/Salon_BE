package com.clowder.booking.controller;

import com.clowder.booking.dto.request.BookingDTO;
import com.clowder.booking.dto.request.NotificationDTO;
import com.clowder.booking.mapper.NotificationMapper;
import com.clowder.booking.model.Notification;
import com.clowder.booking.service.NotificationService;
import com.clowder.booking.service.client.BookingClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications/salon-owner")
public class SalonNotificationController {

  private final NotificationService notificationService;
  private final BookingClient bookingClient;

  @GetMapping("/salon/{salonId}")
  public ResponseEntity<List<NotificationDTO>> getNotificationsBySalonId(
      @PathVariable Long salonId) {
    List<Notification> notifications = notificationService.getAllNotificationsBySalonId(salonId);

    List<NotificationDTO> notificationDTOS =
        notifications.stream()
            .map(
                notification -> {
                  BookingDTO bookingDTO =
                      bookingClient.getBookingsById(notification.getBookingId()).getBody();
                  return NotificationMapper.toDTO(notification, bookingDTO);
                })
            .toList();
    return ResponseEntity.ok(notificationDTOS);
  }
}
