package com.clowder.service.impl;

import com.clowder.dto.request.SalonDTO;
import com.clowder.dto.request.UserDTO;
import com.clowder.model.Salon;
import com.clowder.repository.SalonRepository;
import com.clowder.service.SalonService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalonServiceImpl implements SalonService {

  private final SalonRepository salonRepository;

  @Override
  public Salon createSalon(SalonDTO salon, UserDTO user) {
    if (salonRepository.existsByNameAndAddressAndCity(
        salon.getName(), salon.getAddress(), salon.getCity())) {
      throw new IllegalArgumentException(
          "Salon with the same name, address, and city already exists");
    }

    Salon s = new Salon();
    s.setName(salon.getName());
    s.setAddress(salon.getAddress());
    s.setCity(salon.getCity());
    s.setPhoneNumber(salon.getPhoneNumber());
    s.setEmail(salon.getEmail());
    s.setImages(salon.getImages());
    s.setOwnerId(user.getId());
    s.setOpeningTime(salon.getOpeningTime());
    s.setClosingTime(salon.getClosingTime());

    return salonRepository.save(s);
  }

  @Override
  public Salon updateSalon(SalonDTO salon, UserDTO user, Long salonId) {

    Salon existingSalon =
        salonRepository
            .findById(salonId)
            .orElseThrow(() -> new IllegalArgumentException("Salon not found"));

    if (!existingSalon.getOwnerId().equals(user.getId())) {
      throw new IllegalArgumentException(
          "Only the owner of the salon can update salon's information");
    }

    existingSalon.setName(salon.getName());
    existingSalon.setAddress(salon.getAddress());
    existingSalon.setCity(salon.getCity());
    existingSalon.setImages(salon.getImages());
    existingSalon.setOwnerId(user.getId());
    existingSalon.setPhoneNumber(salon.getPhoneNumber());
    existingSalon.setEmail(salon.getEmail());
    existingSalon.setOpeningTime(salon.getOpeningTime());
    existingSalon.setClosingTime(salon.getClosingTime());

    return salonRepository.save(existingSalon);
  }

  @Override
  public List<Salon> getSalons() {
    return salonRepository.findAll();
  }

  @Override
  public Salon getSalonById(Long salonId) {
    return salonRepository
        .findById(salonId)
        .orElseThrow(() -> new IllegalArgumentException("Salon not found"));
  }

  @Override
  public List<Salon> getSalonsByOwnerId(Long ownerId) {
    return salonRepository.findByOwnerId(ownerId);
  }

  @Override
  public List<Salon> getSalonsByCity(String city) {
    return salonRepository.searchSalons(city);
  }
}
