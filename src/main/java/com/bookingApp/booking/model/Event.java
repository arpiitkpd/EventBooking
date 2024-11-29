package com.bookingApp.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;


@Document(collection = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

        @Id
        private ObjectId id;

        @NonNull
        @DBRef
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

        @Indexed
        private String category;

    }


