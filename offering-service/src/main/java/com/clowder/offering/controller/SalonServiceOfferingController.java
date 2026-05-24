package com.clowder.booking.controller;

import com.clowder.booking.dto.request.CategoryDTO;
import com.clowder.booking.dto.request.SalonDTO;
import com.clowder.booking.dto.request.ServiceDTO;
import com.clowder.booking.model.ServiceOffering;
import com.clowder.booking.service.ServiceOfferingService;
import com.clowder.booking.service.client.CategoryClient;
import com.clowder.booking.service.client.SalonClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/service-offering/salon-owner")
public class SalonServiceOfferingController {

  private final ServiceOfferingService serviceOfferingService;
  private final SalonClient salonClient;
  private final CategoryClient categoryClient;

  @PostMapping()
  public ResponseEntity<ServiceOffering> createServiceOffering(
      @RequestBody ServiceDTO serviceDTO, @RequestHeader("Authorization") String jwt) {

    SalonDTO salonDTO = (SalonDTO) salonClient.getSalonsByOwnerId(jwt).getBody();

    CategoryDTO category =
        categoryClient
            .getCategoriesByIdAndSalonId(serviceDTO.getCategoryId(), salonDTO.getId())
            .getBody();

    ServiceOffering serviceOffering =
        serviceOfferingService.createService(salonDTO, serviceDTO, category);
    return ResponseEntity.ok(serviceOffering);
  }

  @PostMapping("/{id}")
  public ResponseEntity<ServiceOffering> updateServiceOffering(
      @PathVariable Long id, @RequestBody ServiceOffering serviceOffering) {

    ServiceOffering updatedService = serviceOfferingService.updateService(id, serviceOffering);
    return ResponseEntity.ok(updatedService);
  }
}
