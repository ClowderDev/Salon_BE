package com.clowder.offering.service;

import com.clowder.offering.dto.request.CategoryDTO;
import com.clowder.offering.dto.request.SalonDTO;
import com.clowder.offering.dto.request.ServiceDTO;
import com.clowder.offering.model.ServiceOffering;
import java.util.Set;

public interface ServiceOfferingService {

  ServiceOffering createService(SalonDTO salonDTO, ServiceDTO serviceDTO, CategoryDTO categoryDTO);

  ServiceOffering updateService(Long serviceId, ServiceOffering serviceOffering);

  Set<ServiceOffering> getAllServicesBySalonId(Long salonId, Long categoryId);

  Set<ServiceOffering> getAllServicesByIds(Set<Long> ids);

  ServiceOffering getServiceById(Long id);
}
