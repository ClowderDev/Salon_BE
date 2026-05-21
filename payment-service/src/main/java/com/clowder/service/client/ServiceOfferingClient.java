package com.clowder.service.client;

import com.clowder.dto.request.ServiceDTO;
import java.util.Set;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("SERVICE-OFFERING")
public interface ServiceOfferingClient {

  @GetMapping("/api/service-offering/{id}")
  public ResponseEntity<ServiceDTO> getServicesById(@PathVariable Set<Long> id);
}
