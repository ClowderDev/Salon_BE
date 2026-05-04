package com.clowder.mapper;

import com.clowder.dto.request.SalonDTO;
import com.clowder.model.Salon;

public class SalonMapper {

  public static SalonDTO mapSalonToDTO(Salon salon) {
    SalonDTO dto = new SalonDTO();
    dto.setId(salon.getId());
    dto.setName(salon.getName());
    dto.setAddress(salon.getAddress());
    dto.setCity(salon.getCity());
    dto.setPhoneNumber(salon.getPhoneNumber());
    dto.setEmail(salon.getEmail());
    dto.setImages(salon.getImages());
    dto.setOpeningTime(salon.getOpeningTime());
    dto.setClosingTime(salon.getClosingTime());
    return dto;
  }
}
