package com.bookingApp.booking.dto;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Data
@Component
public class BookingRequest {
    private ObjectId eventId;
    private double numberOfSeats;
}
