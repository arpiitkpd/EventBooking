package com.bookingApp.booking.controllers;

import com.bookingApp.booking.dto.UserUpdateRequest;
import com.bookingApp.booking.model.Booking;
import com.bookingApp.booking.model.Event;
import com.bookingApp.booking.model.User;
import com.bookingApp.booking.service.EventService;
import com.bookingApp.booking.service.UserService;
import com.bookingApp.booking.utilities.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<User>> getUser() {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            User dbUser =  userService.findByUserName(userName);
            if(dbUser!=null) {
                return new ResponseEntity<>(
                        new ApiResponse<>(true, "profile gets successfully",dbUser),
                        HttpStatus.OK
                );
            }
            else return new ResponseEntity<>(
                    new ApiResponse<>(false, "profile fetching failed",null),
                    HttpStatus.BAD_REQUEST
            );
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<?>> updateUser(@RequestBody UserUpdateRequest user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User dbUser = userService.findByUserName(userName);
        if(dbUser!=null){
            if(user.getUsername()!=null) dbUser.setUsername(user.getUsername());
            if(user.getPassword()!=null) dbUser.setPassword(user.getPassword());
            if(user.getEmail()!=null) dbUser.setEmail((user.getEmail()));

            userService.saveUser(dbUser);
            return new ResponseEntity<>(
                    new ApiResponse<>(true, "updated succesfully",null),
                    HttpStatus.OK
            );
        }
        return new ResponseEntity<>(
                new ApiResponse<>(false, "updated not requested",null),
                HttpStatus.BAD_REQUEST
        );

    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<?> deleteUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.deleteUser(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwtToken", null)
                .httpOnly(true)
                .path("/")
                .maxAge(0) // Expire immediately
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ApiResponse(true, "Logout successful", null));
    }

    @GetMapping("/user-events")
    public ResponseEntity<ApiResponse<List<Event>>> getUSerBookings(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User dbUser = userService.findByUserName(userName);

        if(dbUser!=null){
            List<Event> events = eventService.findByUser(dbUser);
            return new ResponseEntity<>(
                    new ApiResponse<>(true, "Fetched events of user",events),
                    HttpStatus.OK
            );
        }else{
            return new ResponseEntity<>(
                    new ApiResponse<>(false, "User is not authorized",null),
                    HttpStatus.UNAUTHORIZED
            );
        }
    }


}
