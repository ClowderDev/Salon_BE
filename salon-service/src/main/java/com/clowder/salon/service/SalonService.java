package com.clowder.booking.service;

import com.clowder.booking.dto.request.SalonDTO;
import com.clowder.booking.dto.request.UserDTO;
import com.clowder.booking.model.Salon;
import java.util.List;

public interface SalonService {
  Salon createSalon(SalonDTO salon, UserDTO user);

  Salon updateSalon(SalonDTO salon, UserDTO user, Long salonId);

  List<Salon> getSalons();

  Salon getSalonById(Long salonId);

  List<Salon> getSalonsByOwnerId(Long ownerId);

  List<Salon> getSalonsByCity(String city);
}
