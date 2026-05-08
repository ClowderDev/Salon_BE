package com.clowder.service;

import com.clowder.dto.request.CategoryDTO;
import com.clowder.dto.request.SalonDTO;
import com.clowder.dto.request.ServiceDTO;
import com.clowder.model.ServiceOffering;
import java.util.Set;

public interface ServiceOfferingService {

  ServiceOffering createService(SalonDTO salonDTO, ServiceDTO serviceDTO, CategoryDTO categoryDTO);

  ServiceOffering updateService(Long serviceId, ServiceOffering serviceOffering);

  Set<ServiceOffering> getAllServicesBySalonId(Long salonId, Long categoryId);

  Set<ServiceOffering> getAllServicesByIds(Set<Long> ids);

  ServiceOffering getServiceById(Long id);
}
