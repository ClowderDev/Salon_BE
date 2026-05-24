package com.clowder.notification.dto.request;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationDTO {

  private Long id;

  private String type;

  private Boolean isRead = false;

  private String description;

  private Long userId;

  private Long bookingId;

  private Long salonId;

  private LocalDateTime createdAt;

  private BookingDTO booking;
}
