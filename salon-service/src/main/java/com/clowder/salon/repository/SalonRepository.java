package com.clowder.booking.repository;

import com.clowder.booking.model.Salon;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SalonRepository extends JpaRepository<Salon, Long> {

  List<Salon> findByOwnerId(Long id);

  @Query(
      "SELECT s FROM Salon s WHERE "
          + "LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
          + "LOWER(s.address) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
          + "LOWER(s.city) LIKE LOWER(CONCAT('%', :keyword, '%'))")
  List<Salon> searchSalons(@Param("keyword") String keyword);

  boolean existsByNameAndAddressAndCity(String salonName, String address, String city);
}
