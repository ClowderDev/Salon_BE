package com.clowder.salon.mapper;

import com.clowder.salon.dto.request.SalonDTO;
import com.clowder.salon.model.Salon;

public class SalonMapper {

  public static SalonDTO toDto(Salon salon) {
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
