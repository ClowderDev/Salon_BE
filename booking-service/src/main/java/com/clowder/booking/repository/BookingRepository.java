package com.clowder.booking.repository;

import com.clowder.booking.model.Booking;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
  List<Booking> findByCustomerId(Long customerId);

  List<Booking> findBySalonId(Long salonId);
}
