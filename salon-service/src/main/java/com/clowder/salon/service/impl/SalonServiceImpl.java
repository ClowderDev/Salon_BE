package com.clowder.salon.service.impl;

import com.clowder.common.dto.shared.UserDTO;
import com.clowder.salon.dto.request.SalonRequest;
import com.clowder.salon.model.Salon;
import com.clowder.salon.repository.SalonRepository;
import com.clowder.salon.service.SalonService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalonServiceImpl implements SalonService {

  private final SalonRepository salonRepository;

  @Override
  @CacheEvict(value = {"salons_all", "salons_city", "salons_owner"}, allEntries = true)
  public Salon createSalon(SalonRequest salon, UserDTO user) {
    if (salonRepository.existsByNameAndAddressAndCity(
        salon.getName(), salon.getAddress(), salon.getCity())) {
      throw new IllegalArgumentException(
          "Salon with the same name, address, and city already exists");
    }

    Salon newSalon = new Salon();
    newSalon.setName(salon.getName());
    newSalon.setAddress(salon.getAddress());
    newSalon.setCity(salon.getCity());
    newSalon.setPhoneNumber(salon.getPhoneNumber());
    newSalon.setEmail(salon.getEmail());
    newSalon.setImages(salon.getImages());
    newSalon.setOwnerId(user.getId());
    newSalon.setOpeningTime(salon.getOpeningTime());
    newSalon.setClosingTime(salon.getClosingTime());

    return salonRepository.save(newSalon);
  }

  @Override
  @Caching(evict = {
    @CacheEvict(value = "salon", key = "#salonId"),
    @CacheEvict(value = {"salons_all", "salons_city", "salons_owner"}, allEntries = true)
  })
  public Salon updateSalon(SalonRequest salon, UserDTO user, Long salonId) {

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
  @Cacheable(value = "salons_all")
  public List<Salon> getSalons() {
    return salonRepository.findAll();
  }

  @Override
  @Cacheable(value = "salon", key = "#salonId")
  public Salon getSalonById(Long salonId) {
    return salonRepository
        .findById(salonId)
        .orElseThrow(() -> new IllegalArgumentException("Salon not found"));
  }

  @Override
  @Cacheable(value = "salons_owner", key = "#ownerId")
  public List<Salon> getSalonsByOwnerId(Long ownerId) {
    return salonRepository.findByOwnerId(ownerId);
  }

  @Override
  @Cacheable(value = "salons_city", key = "#city")
  public List<Salon> getSalonsByCity(String city) {
    return salonRepository.searchSalons(city);
  }
}
