package com.bookingApp.booking.controllers;


import com.bookingApp.booking.model.User;
import com.bookingApp.booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicControllers {

    @Autowired
    UserService userService;

    @GetMapping("/health")
    public String healtCheck(){
        return "OK, Public controller working fine";
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody User user){
        try {
            boolean isUserSaved = userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(isUserSaved);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/login")
    public ResponseEntity<Object> login(@RequestBody User user){
        try {
            String isUserVerify = userService.verify(user);
            System.out.println(isUserVerify);
            return ResponseEntity.status(HttpStatus.OK).body(isUserVerify);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

}
