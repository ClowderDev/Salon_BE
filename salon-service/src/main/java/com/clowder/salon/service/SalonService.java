package com.clowder.salon.service;

import com.clowder.common.dto.shared.UserDTO;
import com.clowder.salon.dto.request.SalonRequest;
import com.clowder.salon.model.Salon;
import java.util.List;

public interface SalonService {
  Salon createSalon(SalonRequest salon, UserDTO user);

  Salon updateSalon(SalonRequest salon, UserDTO user, Long salonId);

  List<Salon> getSalons();

  Salon getSalonById(Long salonId);

  List<Salon> getSalonsByOwnerId(Long ownerId);

  List<Salon> getSalonsByCity(String city);
}
