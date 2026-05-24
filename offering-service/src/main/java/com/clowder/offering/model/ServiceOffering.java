package com.clowder.offering.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "service_offerings")
public class ServiceOffering extends BaseEntity {

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private int price;

  @Column(nullable = false)
  private int duration;

  @Column(nullable = false)
  private Long salonId;

  @Column(nullable = false)
  private Long categoryId;

  private String image;
}
