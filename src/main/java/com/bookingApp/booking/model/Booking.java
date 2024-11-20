package com.bookingApp.booking.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "bookings")
@Data
@NoArgsConstructor
public class Booking {
    @Id
    private ObjectId id;

    @NonNull
    @DBRef
    private User user;

    @NonNull
    @DBRef
    private Event event;

    @NonNull
    private double seats;

    @NonNull
    private String bookingDate = String.valueOf(LocalDate.now());

    @NonNull
    private boolean paymentStatus;


}
