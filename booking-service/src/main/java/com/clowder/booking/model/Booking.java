package com.clowder.booking.model;

import com.clowder.booking.enums.BookingStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bookings")
public class Booking extends BaseEntity {

  private Long salonId;

  private Long customerId;

  private LocalDateTime startTime;

  private LocalDateTime endTime;

  private Set<Long> serviceIds;

  private BookingStatus status = BookingStatus.PENDING;

  private int totalPrice;
}
