package com.bookingApp.booking.service;

import com.bookingApp.booking.model.Event;
import com.bookingApp.booking.model.User;
import com.bookingApp.booking.repository.EventRepository;
import com.bookingApp.booking.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

   @Autowired
   private UserService userService;

    public void createEvent(Event event) {
        eventRepository.save(event);
    }

    public Optional<Event> getEventById(ObjectId id) {
        return eventRepository.findById(id);
    }

    public List<Event> getEventByName(String name) {
        return eventRepository.findByName(name);
    }

    public List<Event> getEventByLocation(String location) {
        return eventRepository.findByLocation(location);
    }

    public void updateEvent(Event event, ObjectId id) {
        Optional<Event> dbEvent = eventRepository.findById(id);
        eventRepository.save(event);

    }

    public void deleteEvent(ObjectId id) {
        Optional<Event> dbEvent = eventRepository.findById(id);

        if (dbEvent.isPresent()) {
            eventRepository.deleteById(dbEvent.get().getId());
        } else {
            throw new IllegalArgumentException("Event with ID " + id + " not found.");
        }
    }
}
