package com.clowder.booking.mapper;

import com.clowder.booking.dto.response.BookingDTO;
import com.clowder.booking.model.Booking;

public class BookingMapper {
  public static BookingDTO toDto(Booking booking) {
    BookingDTO bookingDTO = new BookingDTO();
    bookingDTO.setId(booking.getId());
    bookingDTO.setCustomerId(booking.getCustomerId());
    bookingDTO.setStatus(booking.getStatus());
    bookingDTO.setEndTime(booking.getEndTime());
    bookingDTO.setStartTime(booking.getStartTime());
    bookingDTO.setSalonId(booking.getSalonId());
    bookingDTO.setServiceIds(booking.getServiceIds());
    bookingDTO.setTotalPrice(booking.getTotalPrice());
    return bookingDTO;
  }
}
