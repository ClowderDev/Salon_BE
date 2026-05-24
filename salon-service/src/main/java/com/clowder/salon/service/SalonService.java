package com.clowder.salon.service;

import com.clowder.salon.dto.request.SalonDTO;
import com.clowder.salon.dto.request.UserDTO;
import com.clowder.salon.model.Salon;
import java.util.List;

public interface SalonService {
  Salon createSalon(SalonDTO salon, UserDTO user);

  Salon updateSalon(SalonDTO salon, UserDTO user, Long salonId);

  List<Salon> getSalons();

  Salon getSalonById(Long salonId);

  List<Salon> getSalonsByOwnerId(Long ownerId);

  List<Salon> getSalonsByCity(String city);
}
