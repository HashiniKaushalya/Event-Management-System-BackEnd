package com.moratuwa.events.controllers;

import com.moratuwa.events.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AttributeController {

    @Autowired
    EventService eventService;


    @GetMapping(value = "/attribute/event_types")
    public ResponseEntity<?> getEventTypes(){
        return new ResponseEntity<>(eventService.getEventTypes(), HttpStatus.OK);
    }

    @GetMapping(value = "/attribute/venues")
    public ResponseEntity<?> getVenues(){

        return new ResponseEntity<>(eventService.getVenues(), HttpStatus.OK);
    }
}
