package com.bookingApp.booking.dto;

import lombok.Data;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class UserUpdateRequest {
    @Id
    private ObjectId id;

    @Indexed(unique = true)
    private String username;


    private String password;

    @Indexed(unique = true)
    private String email;

    private List<String> roles;
}
