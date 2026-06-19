package com.clowder.offering.service.impl;

import com.clowder.common.dto.shared.CategoryDTO;
import com.clowder.common.dto.shared.SalonDTO;
import com.clowder.common.dto.shared.ServiceDTO;
import com.clowder.offering.model.ServiceOffering;
import com.clowder.offering.repository.ServiceOfferingRepository;
import com.clowder.offering.service.ServiceOfferingService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceOfferingServiceImpl implements ServiceOfferingService {

  private final ServiceOfferingRepository serviceOfferingRepository;

  @Override
  @CacheEvict(value = "offerings_salon", key = "#salonDTO.id")
  public ServiceOffering createService(
      SalonDTO salonDTO, ServiceDTO serviceDTO, CategoryDTO categoryDTO) {

    if (salonDTO == null || serviceDTO == null || categoryDTO == null) {
      throw new IllegalArgumentException(
          "Salon, Service, and Category information must be provided.");
    }

    if (serviceDTO.getId() != null
        && serviceOfferingRepository.findById(serviceDTO.getId()).isPresent()) {
      throw new IllegalArgumentException("Service with the same ID already exists");
    }

    ServiceOffering serviceOffering = new ServiceOffering();
    serviceOffering.setImage(serviceDTO.getImage());
    serviceOffering.setSalonId(salonDTO.getId());
    serviceOffering.setName(serviceDTO.getName());
    serviceOffering.setCategoryId(categoryDTO.getId());
    serviceOffering.setPrice(serviceDTO.getPrice());
    serviceOffering.setDescription(serviceDTO.getDescription());
    serviceOffering.setDuration(serviceDTO.getDuration());

    return serviceOfferingRepository.save(serviceOffering);
  }

  @Override
  @Caching(evict = {
    @CacheEvict(value = "offering", key = "#serviceId"),
    @CacheEvict(value = "offerings_salon", key = "#result.salonId")
  })
  public ServiceOffering updateService(Long serviceId, ServiceOffering service) {

    if (serviceId == null || service.getId() == null) {
      throw new IllegalArgumentException("Service ID must be provided for update.");
    }

    ServiceOffering existedServiceOffering =
        serviceOfferingRepository
            .findById(serviceId)
            .orElseThrow(
                () -> new IllegalArgumentException("Service not found with id: " + serviceId));

    existedServiceOffering.setImage(service.getImage());
    existedServiceOffering.setName(service.getName());
    existedServiceOffering.setDescription(service.getDescription());
    existedServiceOffering.setPrice(service.getPrice());
    existedServiceOffering.setDuration(service.getDuration());

    return serviceOfferingRepository.save(existedServiceOffering);
  }

  @Override
  @Cacheable(value = "offerings_salon", key = "#salonId + (#categoryId != null ? '-' + #categoryId : '')")
  public Set<ServiceOffering> getAllServicesBySalonId(Long salonId, Long categoryId) {
    if (categoryId != null) {
      return serviceOfferingRepository.findBySalonIdAndCategoryId(salonId, categoryId);
    }
    return serviceOfferingRepository.findBySalonId(salonId);
  }

  @Override
  public Set<ServiceOffering> getAllServicesByIds(Set<Long> ids) {
    List<ServiceOffering> services = serviceOfferingRepository.findAllById(ids);
    return new HashSet<>(services);
  }

  @Override
  @Cacheable(value = "offering", key = "#id")
  public ServiceOffering getServiceById(Long id) {
    ServiceOffering serviceOffering =
        serviceOfferingRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Service not found with id: " + id));

    return serviceOffering;
  }
}
