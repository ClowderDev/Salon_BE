package com.clowder.controller;

import com.clowder.dto.request.SalonDTO;
import com.clowder.dto.request.UserDTO;
import com.clowder.mapper.SalonMapper;
import com.clowder.model.Salon;
import com.clowder.service.SalonService;
import com.clowder.service.client.UserClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/salons")
public class SalonController {

  private final SalonService salonService;
  private final UserClient userClient;

  @PostMapping
  public ResponseEntity<SalonDTO> createSalon(
      @RequestBody SalonDTO salon, @RequestHeader("Authorization") String jwt) {
    UserDTO userDTO = userClient.getUserProfile(jwt).getBody();
    Salon createdSalon = salonService.createSalon(salon, userDTO);
    SalonDTO dto = SalonMapper.mapSalonToDTO(createdSalon);
    return ResponseEntity.ok(dto);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<SalonDTO> updateSalon(
      @RequestBody SalonDTO salon,
      @PathVariable("id") Long salonId,
      @RequestHeader("Authorization") String jwt) {
    if (salon.getId() == null) {
      return ResponseEntity.badRequest().build();
    }

    UserDTO userDTO = userClient.getUserProfile(jwt).getBody();

    Salon updatedSalon = salonService.updateSalon(salon, userDTO, salonId);
    SalonDTO dto = SalonMapper.mapSalonToDTO(updatedSalon);
    return ResponseEntity.ok(dto);
  }

  @GetMapping
  public ResponseEntity<List<SalonDTO>> getAllSalons(UserDTO user) {
    List<Salon> salons = salonService.getSalons();
    List<SalonDTO> dtos = salons.stream().map(SalonMapper::mapSalonToDTO).toList();
    return ResponseEntity.ok(dtos);
  }

  @GetMapping("/{salonId}")
  public ResponseEntity<SalonDTO> getSalonById(@PathVariable Long salonId) {
    Salon salon = salonService.getSalonById(salonId);
    SalonDTO dto = SalonMapper.mapSalonToDTO(salon);
    return ResponseEntity.ok(dto);
  }

  @GetMapping("/owner")
  public ResponseEntity<List<SalonDTO>> getSalonsByOwnerId(
      @RequestHeader("Authorization") String jwt) {
    UserDTO userDTO = userClient.getUserProfile(jwt).getBody();

    if (userDTO == null) {
      return ResponseEntity.badRequest().build();
    }

    List<Salon> salons = salonService.getSalonsByOwnerId(userDTO.getId());
    List<SalonDTO> dtos = salons.stream().map(SalonMapper::mapSalonToDTO).toList();
    return ResponseEntity.ok(dtos);
  }

  @GetMapping("/search")
  public ResponseEntity<List<SalonDTO>> searchSalons(@RequestParam("city") String city) {
    List<SalonDTO> salons =
        salonService.getSalonsByCity(city).stream().map(SalonMapper::mapSalonToDTO).toList();
    return ResponseEntity.ok(salons);
  }
}
