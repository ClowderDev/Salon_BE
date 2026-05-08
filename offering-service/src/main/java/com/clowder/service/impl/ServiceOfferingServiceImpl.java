package com.clowder.service.impl;

import com.clowder.dto.request.CategoryDTO;
import com.clowder.dto.request.SalonDTO;
import com.clowder.dto.request.ServiceDTO;
import com.clowder.model.ServiceOffering;
import com.clowder.repository.ServiceOfferingRepository;
import com.clowder.service.ServiceOfferingService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceOfferingServiceImpl implements ServiceOfferingService {

  private final ServiceOfferingRepository serviceOfferingRepository;

  @Override
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
  public Set<ServiceOffering> getAllServicesBySalonId(Long salonId, Long categoryId) {
    Set<ServiceOffering> services = serviceOfferingRepository.findBySalonId(salonId);

    if (categoryId != null) {
      services =
          services.stream()
              .filter(s -> s.getCategoryId() != null && s.getCategoryId().equals(categoryId))
              .collect(Collectors.toSet());
    }

    return services;
  }

  @Override
  public Set<ServiceOffering> getAllServicesByIds(Set<Long> ids) {
    List<ServiceOffering> services = serviceOfferingRepository.findAllById(ids);
    return new HashSet<>(services);
  }

  @Override
  public ServiceOffering getServiceById(Long id) {
    ServiceOffering serviceOffering =
        serviceOfferingRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Service not found with id: " + id));

    return serviceOffering;
  }
}
