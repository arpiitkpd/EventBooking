package com.bookingApp.booking.service;

import com.bookingApp.booking.dto.EventCreateDTO;
import com.bookingApp.booking.model.Event;
import com.bookingApp.booking.model.User;
import com.bookingApp.booking.repository.EventRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public void updateEvent(EventCreateDTO eventDto, ObjectId id) {
        // Find the event by ID
        Optional<Event> optionalEvent = eventRepository.findById(id);

        if (optionalEvent.isPresent()) { // Check if the event exists
            Event event = optionalEvent.get(); // Extract the Event entity

            // Update the fields
            event.setName(eventDto.getName());
            event.setCategory(eventDto.getCategory());
            event.setLocation(eventDto.getLocation());
            event.setTotalSeats(eventDto.getTotalSeats());
            event.setDescription(eventDto.getDescription());
            event.setPrice(eventDto.getPrice());

            // Save the updated event
            eventRepository.save(event);
        } else {
            throw new IllegalArgumentException("Event not found with ID: " + id);
        }
    }


    public void deleteEvent(ObjectId id) {
        Optional<Event> dbEvent = eventRepository.findById(id);

        if (dbEvent.isPresent()) {
            eventRepository.deleteById(dbEvent.get().getId());
        } else {
            throw new IllegalArgumentException("Event with ID " + id + " not found.");
        }
    }

    public List<Event> queryEvent(String query){
        return eventRepository.findAll().stream()
                .filter(event -> event.getName().toLowerCase().contains(query.toLowerCase()) ||
                        event.getLocation().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Event> findByUser(User dbUser) {
        return eventRepository.findByUser(dbUser);
    }
}
