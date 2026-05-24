package com.clowder.booking.mapper;

import com.clowder.booking.model.Booking;
import com.clowder.common.dto.shared.BookingDTO;

public class BookingMapper {
  public static BookingDTO toDto(Booking booking) {
    BookingDTO dto = new BookingDTO();
    dto.setId(booking.getId());
    dto.setSalonId(booking.getSalonId());
    dto.setStatus(booking.getStatus());
    dto.setStartTime(booking.getStartTime());
    dto.setEndTime(booking.getEndTime());
    dto.setServiceIds(booking.getServiceIds());
    dto.setTotalPrice(booking.getTotalPrice());
    return dto;
  }
}
