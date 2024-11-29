package com.bookingApp.booking.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.stereotype.Component;

@Data
@Component

public class UserLogin {

    private String username;

    private String password;
}
