package com.bookingApp.booking.model;

import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "users")
@Data
public class User {
    @Id
    private ObjectId id;

    @NonNull
    private String username;

    @NonNull
    private String password;

    @Indexed(unique = true)
    private String email;

    private List<String> roles;
}
