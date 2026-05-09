package com.clowder.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class SalonReport {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long salonId;

  private String salonName;
  private Double totalEarnings;
  private Integer totalBookings;
  private Integer cancelledBookings;
  private Double totalRefund;
}
