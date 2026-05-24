package com.clowder.offering.service;

import com.clowder.common.dto.shared.CategoryDTO;
import com.clowder.common.dto.shared.SalonDTO;
import com.clowder.common.dto.shared.ServiceDTO;
import com.clowder.offering.model.ServiceOffering;
import java.util.Set;

public interface ServiceOfferingService {

  ServiceOffering createService(SalonDTO salonDTO, ServiceDTO serviceDTO, CategoryDTO categoryDTO);

  ServiceOffering updateService(Long serviceId, ServiceOffering serviceOffering);

  Set<ServiceOffering> getAllServicesBySalonId(Long salonId, Long categoryId);

  Set<ServiceOffering> getAllServicesByIds(Set<Long> ids);

  ServiceOffering getServiceById(Long id);
}
