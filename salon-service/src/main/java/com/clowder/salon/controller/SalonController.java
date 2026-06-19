package com.clowder.salon.controller;

import com.clowder.common.dto.shared.SalonDTO;
import com.clowder.common.dto.shared.UserDTO;
import com.clowder.salon.dto.request.SalonRequest;
import com.clowder.salon.mapper.SalonMapper;
import com.clowder.salon.model.Salon;
import com.clowder.salon.service.SalonService;
import com.clowder.salon.service.client.UserClient;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Salons", description = "Operations related to salon management and searching")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/salons")
public class SalonController {

  private final SalonService salonService;
  private final UserClient userClient;

  @Operation(summary = "Create a new salon")
  @PostMapping
  public ResponseEntity<SalonDTO> createSalon(
      @RequestBody @Valid SalonRequest salon, @RequestHeader("Authorization") String jwt) {
    UserDTO userDTO = userClient.getUserProfile(jwt).getBody();
    Salon createdSalon = salonService.createSalon(salon, userDTO);
    SalonDTO dto = SalonMapper.toDto(createdSalon);
    return ResponseEntity.ok(dto);
  }

  @Operation(summary = "Update an existing salon")
  @PatchMapping("/{id}")
  public ResponseEntity<SalonDTO> updateSalon(
      @RequestBody @Valid SalonRequest salon,
      @PathVariable("id") Long salonId,
      @RequestHeader("Authorization") String jwt) {
    UserDTO userDTO = userClient.getUserProfile(jwt).getBody();
    Salon updatedSalon = salonService.updateSalon(salon, userDTO, salonId);
    SalonDTO dto = SalonMapper.toDto(updatedSalon);
    return ResponseEntity.ok(dto);
  }

  @Operation(summary = "Get all salons")
  @GetMapping
  public ResponseEntity<List<SalonDTO>> getAllSalons() {
    List<Salon> salons = salonService.getSalons();
    List<SalonDTO> dtos = salons.stream().map(SalonMapper::toDto).toList();
    return ResponseEntity.ok(dtos);
  }

  @Operation(summary = "Get a salon by its ID")
  @GetMapping("/{salonId}")
  public ResponseEntity<SalonDTO> getSalonById(@PathVariable Long salonId) {
    Salon salon = salonService.getSalonById(salonId);
    SalonDTO dto = SalonMapper.toDto(salon);
    return ResponseEntity.ok(dto);
  }

  @Operation(summary = "Get salons owned by the authenticated owner")
  @GetMapping("/owner")
  public ResponseEntity<List<SalonDTO>> getSalonsByOwnerId(
      @RequestHeader("Authorization") String jwt) {
    UserDTO userDTO = userClient.getUserProfile(jwt).getBody();
    if (userDTO == null) {
      return ResponseEntity.badRequest().build();
    }
    List<Salon> salons = salonService.getSalonsByOwnerId(userDTO.getId());
    List<SalonDTO> dtos = salons.stream().map(SalonMapper::toDto).toList();
    return ResponseEntity.ok(dtos);
  }

  @Operation(summary = "Search salons by city name")
  @GetMapping("/search")
  public ResponseEntity<List<SalonDTO>> searchSalons(@RequestParam("city") String city) {
    List<SalonDTO> salons =
        salonService.getSalonsByCity(city).stream().map(SalonMapper::toDto).toList();
    return ResponseEntity.ok(salons);
  }
}
