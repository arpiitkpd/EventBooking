package com.bookingApp.booking.controllers;

import com.bookingApp.booking.model.Event;
import com.bookingApp.booking.model.User;
import com.bookingApp.booking.service.EventService;
import com.bookingApp.booking.service.JWTService;
import com.bookingApp.booking.service.UserService;
import com.bookingApp.booking.utilities.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
@CrossOrigin(origins = "http://localhost:5173")
public class PublicControllers {

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTService jwtService;

    @Autowired
    EventService eventService;


    @GetMapping("/health")
    public ResponseEntity<?> currentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return ResponseEntity.ok(userDetails);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");

    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> registerUser(@RequestBody User user){
        try {
            boolean isUserSaved = userService.registerUser(user);
            if(isUserSaved==false){
                return new ResponseEntity<>(
                        new ApiResponse<>(false, "user register failed",null),
                        HttpStatus.CREATED
                );
            }
            return new ResponseEntity<>(
                    new ApiResponse<>(true, "user register Succesfully",user),
                    HttpStatus.CREATED
            );
        }catch(Exception e){
            return new ResponseEntity<>(
                    new ApiResponse<>(false, "An Exception occurred: " + e.getMessage(), null),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody User loginRequest) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            // Retrieve user details from the authenticated principal
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();


            // Generate JWT token
            String jwtToken = jwtService.generateToken(userDetails.getUsername());

            // Set JWT token as an HTTP-only cookie
            ResponseCookie cookie = ResponseCookie.from("jwtToken", jwtToken)
                    .httpOnly(true)              // Prevent access via JavaScript
                    .secure(true)               // Use HTTPS for secure transmission
                    .path("/")                  // Accessible to all endpoints
                    .maxAge(60 * 60 * 72)       // 72 hours expiration
                    .sameSite("Strict")         // Prevent cross-site usage
                    .build();

            // Add the cookie to the response header
            return ResponseEntity.ok()
                    .header("Set-Cookie", cookie.toString())
                    .body(new ApiResponse<>(true, "User login successfully", null));
        } catch (BadCredentialsException e) {
            // Handle invalid credentials
            return new ResponseEntity<>(
                    new ApiResponse<>(false, "Invalid username or password", null),
                    HttpStatus.UNAUTHORIZED
            );
        } catch (Exception e) {
            // Handle other exceptions
            return new ResponseEntity<>(
                    new ApiResponse<>(false, "An Exception occurred: " + e.getMessage(), null),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/query/{query}")
    public ResponseEntity<ApiResponse<List<Event>>> queryEvents(@PathVariable String query){
        List<Event> events = eventService.queryEvent(query);
        if(events!=null){
            return new ResponseEntity<>(
                    new ApiResponse<>(true, "Fetched events of query",events),
                    HttpStatus.FOUND
            );
        }else{
            return new ResponseEntity<>(
                    new ApiResponse<>(false, "problem in query events",null),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

}
