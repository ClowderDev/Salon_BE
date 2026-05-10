package com.clowder.dto.request;

import com.clowder.enums.BookingStatus;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookingDTO {
  private Long id;

  private Long salonId;

  private Long customerId;

  private LocalDateTime startTime;

  private LocalDateTime endTime;

  private Set<Long> serviceIds;

  private BookingStatus status = BookingStatus.PENDING;

  private int totalPrice;
}
