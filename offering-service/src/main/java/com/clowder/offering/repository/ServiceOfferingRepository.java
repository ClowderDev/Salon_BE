package com.clowder.offering.repository;

import com.clowder.offering.model.ServiceOffering;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceOfferingRepository extends JpaRepository<ServiceOffering, Long> {

  Set<ServiceOffering> findBySalonId(Long salonId);
}
