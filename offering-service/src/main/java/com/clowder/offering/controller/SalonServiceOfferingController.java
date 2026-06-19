package com.clowder.offering.controller;

import com.clowder.common.dto.shared.CategoryDTO;
import com.clowder.common.dto.shared.SalonDTO;
import com.clowder.common.dto.shared.ServiceDTO;
import com.clowder.common.exception.ResourceNotFoundException;
import com.clowder.offering.model.ServiceOffering;
import com.clowder.offering.service.ServiceOfferingService;
import com.clowder.offering.service.client.CategoryClient;
import com.clowder.offering.service.client.SalonClient;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Salon Owner Service Offerings", description = "Salon owner operations related to service offerings")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/service-offering/salon-owner")
public class SalonServiceOfferingController {

  private final ServiceOfferingService serviceOfferingService;
  private final SalonClient salonClient;
  private final CategoryClient categoryClient;

  @Operation(summary = "Create a service offering for the owner's salon")
  @PostMapping()
  public ResponseEntity<ServiceOffering> createServiceOffering(
      @RequestBody @Valid ServiceDTO serviceDTO, @RequestHeader("Authorization") String jwt) {

    List<SalonDTO> salons = salonClient.getSalonsByOwnerId(jwt).getBody();
    if (salons == null || salons.isEmpty()) {
      throw new ResourceNotFoundException("No salon found for this owner");
    }
    SalonDTO salonDTO = salons.get(0);

    CategoryDTO category =
        categoryClient
            .getCategoriesByIdAndSalonId(serviceDTO.getCategoryId(), salonDTO.getId())
            .getBody();

    ServiceOffering serviceOffering =
        serviceOfferingService.createService(salonDTO, serviceDTO, category);
    return ResponseEntity.ok(serviceOffering);
  }

  @Operation(summary = "Update an existing service offering")
  @PutMapping("/{id}")
  public ResponseEntity<ServiceOffering> updateServiceOffering(
      @PathVariable Long id, @RequestBody ServiceOffering serviceOffering) {

    ServiceOffering updatedService = serviceOfferingService.updateService(id, serviceOffering);
    return ResponseEntity.ok(updatedService);
  }
}
