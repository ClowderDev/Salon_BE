package com.clowder.booking.dto.request;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookingSlotDTO {
  private LocalDateTime startTime;
  private LocalDateTime endTime;
}
