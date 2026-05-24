package com.clowder.salon.model;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalTime;
import java.util.List;
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
@Table(name = "salons")
public class Salon extends BaseEntity {

  @Column(nullable = false)
  private String name;

  @ElementCollection private List<String> images;

  @Column(nullable = false)
  private String address;

  @Column(nullable = false)
  private String phoneNumber;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String city;

  @Column(nullable = false)
  private Long ownerId;

  @Column(nullable = false)
  private LocalTime openingTime;

  @Column(nullable = false)
  private LocalTime closingTime;
}
