package com.clowder.booking.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalonReport {

  private String salonName;
  private Double totalEarnings;
  private Integer totalBookings;
  private Integer cancelledBookings;
  private Double totalRefund;
}
