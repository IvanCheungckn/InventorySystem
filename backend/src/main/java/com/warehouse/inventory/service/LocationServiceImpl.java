package com.warehouse.inventory.service;

import com.warehouse.inventory.entity.Location;
import com.warehouse.inventory.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService{

    LocationRepository locationRepository;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public List<Location> getLocations() {
        List<Location> locationList = locationRepository.findAll();
        return locationList;
    }
}
