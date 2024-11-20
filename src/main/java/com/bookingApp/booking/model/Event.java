package com.bookingApp.booking.model;

import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;


@Document(collection = "events")
@Data
public class Event {

        @Id
        private ObjectId id;

        private User user;

        @NonNull
        @Indexed
        private String name;

        @NonNull
        @Indexed
        private String location;

        @NonNull
        @Indexed
        private String date = String.valueOf(LocalDate.now());

        @NonNull
        private double price;

        @NonNull
        private double totalSeats;

        private String description;

        private String category;

    }


