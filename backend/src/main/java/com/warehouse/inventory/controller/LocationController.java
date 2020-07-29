package com.warehouse.inventory.controller;

import com.warehouse.inventory.entity.Location;
import com.warehouse.inventory.model.Response;
import com.warehouse.inventory.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/location")
public class LocationController {
    LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("")
    public ResponseEntity getAllLocations() {
        List<Location> locationList = locationService.getLocations();
        return new ResponseEntity(new Response(true, locationList), HttpStatus.OK);
    }
}
