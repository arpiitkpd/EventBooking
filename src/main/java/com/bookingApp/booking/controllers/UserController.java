package com.bookingApp.booking.controllers;

import com.bookingApp.booking.dto.UserUpdateRequest;
import com.bookingApp.booking.model.Booking;
import com.bookingApp.booking.model.User;
import com.bookingApp.booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> getUser() {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            User dbUser =  userService.findByUserName(userName);
            System.out.println(dbUser);
            if(dbUser!=null) return new ResponseEntity<>(dbUser, HttpStatus.OK);
            else return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody UserUpdateRequest user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User dbUser = userService.findByUserName(userName);
        if(dbUser!=null){
            if(user.getUsername()!=null) dbUser.setUsername(user.getUsername());
            if(user.getPassword()!=null) dbUser.setPassword(user.getPassword());
            if(user.getEmail()!=null) dbUser.setEmail((user.getEmail()));

            userService.saveUser(dbUser);
            return new ResponseEntity<>(dbUser,HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/delete-profile")
    public ResponseEntity<?> deleteUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.deleteUser(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    Have to complete after booking service
    @GetMapping("/bookings")
    public ResponseEntity<List<Booking>> getUSerBookings(){
        return new ResponseEntity<>(new ArrayList<>(null),HttpStatus.NO_CONTENT);
    }


}
