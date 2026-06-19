package com.clowder.booking.service.client;

import com.clowder.booking.service.client.fallback.ServiceOfferingClientFallback;
import com.clowder.common.dto.shared.ServiceDTO;
import java.util.Set;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-offering", fallback = ServiceOfferingClientFallback.class)
public interface ServiceOfferingClient {

  @GetMapping("/api/service-offering/{id}")
  public ResponseEntity<ServiceDTO> getServicesById(@PathVariable Set<Long> id);
}
