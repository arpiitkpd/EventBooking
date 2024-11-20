package com.bookingApp.booking.controllers;

import com.bookingApp.booking.model.Event;
import com.bookingApp.booking.model.User;
import com.bookingApp.booking.repository.UserRepository;
import com.bookingApp.booking.service.EventService;
import com.bookingApp.booking.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
public class EventControllers {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/create-event")
    public String createEvent(@RequestBody Event event){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User dbUser =  userService.findByUserName(userName);
        System.out.println(dbUser);
        event.setUser(dbUser);
        eventService.createEvent(event);
        return "ok";
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
    public void updateById(@RequestBody Event event , @PathVariable ObjectId id){
        eventService.updateEvent(event, id);
    }

    @PutMapping("/delete/{id}")
    public void deleteById(@PathVariable ObjectId id){
        eventService.deleteEvent(id);
    }


}
