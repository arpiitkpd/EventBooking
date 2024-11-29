package com.bookingApp.booking.controllers;

import com.bookingApp.booking.dto.BookingRequest;
import com.bookingApp.booking.model.Booking;
import com.bookingApp.booking.service.BookingService;
import com.bookingApp.booking.utilities.ApiResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.List;

@RestController
@RequestMapping("/booking")
@CrossOrigin(origins = "http://localhost:5173")
public class BookingControllers {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/create-booking")
    public ResponseEntity<ApiResponse<Booking>> createBooking(@RequestBody BookingRequest request) {

        Booking isBooked =  bookingService.createBooking(request);
        if(isBooked!=null){
            return new ResponseEntity<>(
                    new ApiResponse<>(true, "Booking confirmed", isBooked),
                    HttpStatus.OK
            );
        }

        else return new ResponseEntity<>(
                new ApiResponse<>(false, "Booking error ", isBooked),
                HttpStatus.CONFLICT
        );
    }

    @GetMapping("/user-bookings")
    public ResponseEntity<ApiResponse<List<Booking>>>getUserBooking(){
       List<Booking> bookingList = bookingService.getUserBooking();
       if(bookingList!=null){
           return new ResponseEntity<>(
                   new ApiResponse<>(true, "Booking fetched", bookingList),
                   HttpStatus.OK
           );
       }else{
           return new ResponseEntity<>(
                   new ApiResponse<>(false, "booking fetching failed", null),
                   HttpStatus.EXPECTATION_FAILED
           );
       }

    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<Boolean> deleteBookingById(@PathVariable ObjectId id){
        boolean isDeleted =bookingService.deleteBookingById(id);
        return ResponseEntity.ok(isDeleted);
    }
}
