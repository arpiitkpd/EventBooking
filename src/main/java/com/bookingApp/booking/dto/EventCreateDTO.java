package com.bookingApp.booking.dto;

import com.bookingApp.booking.model.User;
import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;


@Data
public class EventCreateDTO {

    @NonNull
    private String name;

    @NonNull
    private String category;

    @NonNull
    private String location;

    @NonNull
    private Integer totalSeats;

    @NonNull
    private Double price;


    private String description;
}
