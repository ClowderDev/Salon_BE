package com.clowder.controller;

import com.clowder.dto.request.CategoryDTO;
import com.clowder.dto.request.SalonDTO;
import com.clowder.dto.request.ServiceDTO;
import com.clowder.model.ServiceOffering;
import com.clowder.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/service-offering/salon-owner")
public class SalonServiceOfferingController {

  private final ServiceOfferingService serviceOfferingService;

  @PostMapping()
  public ResponseEntity<ServiceOffering> createServiceOffering(@RequestBody ServiceDTO serviceDTO) {

    SalonDTO salon = new SalonDTO();
    salon.setId(1L);

    CategoryDTO category = new CategoryDTO();
    category.setId(serviceDTO.getCategoryId());

    ServiceOffering serviceOffering =
        serviceOfferingService.createService(salon, serviceDTO, category);
    return ResponseEntity.ok(serviceOffering);
  }

  @PostMapping("/{id}")
  public ResponseEntity<ServiceOffering> updateServiceOffering(
      @PathVariable Long id, @RequestBody ServiceOffering serviceOffering) {

    ServiceOffering updatedService = serviceOfferingService.updateService(id, serviceOffering);
    return ResponseEntity.ok(updatedService);
  }
}
