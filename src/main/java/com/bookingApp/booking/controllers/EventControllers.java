package com.bookingApp.booking.controllers;

import com.bookingApp.booking.dto.EventCreateDTO;
import com.bookingApp.booking.model.Event;
import com.bookingApp.booking.model.User;
import com.bookingApp.booking.repository.UserRepository;
import com.bookingApp.booking.service.EventService;
import com.bookingApp.booking.service.UserService;
import com.bookingApp.booking.utilities.ApiResponse;
import com.mongodb.DuplicateKeyException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
@CrossOrigin(origins = "http://localhost:5173")
public class EventControllers {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/create-event")
    public ResponseEntity<ApiResponse<Event>> createEvent(@RequestBody EventCreateDTO eventDto) {
        try {
            // Get authenticated user's username
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            System.out.println(userName);

            // Fetch user details
            User dbUser = userService.findByUserName(userName);
            System.out.println(dbUser);


            if (dbUser == null) {
                return new ResponseEntity<>(
                        new ApiResponse<>(false, "User Unauthorized", null),
                        HttpStatus.UNAUTHORIZED
                );
            }

            // Create event and map fields from DTO
            Event event = new Event();
            event.setUser(dbUser);
            event.setName(eventDto.getName());
            event.setCategory(eventDto.getCategory());
            event.setLocation(eventDto.getLocation());
            event.setTotalSeats(eventDto.getTotalSeats());
            event.setDescription(eventDto.getDescription());
            event.setPrice(eventDto.getPrice());

            System.out.println(event);

            // Save event
            eventService.createEvent(event);

            return new ResponseEntity<>(
                    new ApiResponse<>(true, "Event created successfully", event),
                    HttpStatus.CREATED
            );
        } catch (DuplicateKeyException e) {
            // Handle duplicate key errors
            return new ResponseEntity<>(
                    new ApiResponse<>(false, "Duplicate key error: " + e.getMessage(), null),
                    HttpStatus.CONFLICT
            );
        } catch (Exception e) {
            // Handle unexpected errors
            return new ResponseEntity<>(
                    new ApiResponse<>(false, "An exception occurred: " + e.getMessage(), null),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("id/{id}")
    public Event getEventById(@PathVariable ObjectId id){
        Optional<Event> event = eventService.getEventById(id);
        return event.orElse(null);
    }

    @GetMapping("name/{name}")
    public List<Event> getEventByName(@PathVariable String name){
        List<Event> events = eventService.getEventByName(name);
        if(events!=null) return events;
        else return null;
    }

    @GetMapping("location/{location}")
    public List<Event> getEventByLocation(@PathVariable String location){
        List<Event> events = eventService.getEventByLocation(location);
        if(events!=null) return events;
        else return null;
    }

    @PutMapping("/update/{id}")
    public void updateById(@RequestBody EventCreateDTO event , @PathVariable ObjectId id){
        eventService.updateEvent(event, id);
    }

    @PutMapping("/delete/{id}")
    public void deleteById(@PathVariable ObjectId id){
        eventService.deleteEvent(id);
    }





}
