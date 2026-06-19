package com.clowder.offering.controller;

import com.clowder.offering.model.ServiceOffering;
import com.clowder.offering.service.ServiceOfferingService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Service Offerings", description = "Public operations related to service offerings")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/service-offering")
public class ServiceOfferingController {

  private final ServiceOfferingService serviceOfferingService;

  @Operation(summary = "Get service offerings for a salon, optionally filtered by category")
  @GetMapping("/salon/{salonId}")
  public ResponseEntity<Set<ServiceOffering>> getServicesBySalonId(
      @PathVariable Long salonId, @RequestParam(required = false) Long categoryId) {
    Set<ServiceOffering> serviceOfferings =
        serviceOfferingService.getAllServicesBySalonId(salonId, categoryId);
    return ResponseEntity.ok(serviceOfferings);
  }

  @Operation(summary = "Get a service offering by ID")
  @GetMapping("/{id}")
  public ResponseEntity<ServiceOffering> getServicesById(@PathVariable Long id) {
    ServiceOffering serviceOfferings = serviceOfferingService.getServiceById(id);
    return ResponseEntity.ok(serviceOfferings);
  }

  @Operation(summary = "Get multiple service offerings by a set of IDs")
  @GetMapping("/list/{ids}")
  public ResponseEntity<Set<ServiceOffering>> getServicesByIds(@PathVariable Set<Long> ids) {
    Set<ServiceOffering> serviceOfferings = serviceOfferingService.getAllServicesByIds(ids);
    return ResponseEntity.ok(serviceOfferings);
  }
}
