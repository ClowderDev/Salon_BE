package com.clowder.service;

import com.clowder.dto.request.SalonDTO;
import com.clowder.dto.request.UserDTO;
import com.clowder.model.Salon;
import java.util.List;

public interface SalonService {
  Salon createSalon(SalonDTO salon, UserDTO user);

  Salon updateSalon(SalonDTO salon, UserDTO user, Long salonId);

  List<Salon> getSalons();

  Salon getSalonById(Long salonId);

  List<Salon> getSalonsByOwnerId(Long ownerId);

  List<Salon> getSalonsByCity(String city);
}
