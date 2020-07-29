package com.warehouse.inventory;

import com.warehouse.inventory.controller.LocationController;
import com.warehouse.inventory.entity.Location;
import com.warehouse.inventory.model.Response;
import com.warehouse.inventory.service.LocationService;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class LocationControllerTest {
    @Mock
    LocationService locationService;

    @InjectMocks
    private LocationController locationController;

    LocalDateTime createdDateTime;
    LocalDateTime updatedDateTime;

    @Before
    public void init() {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        createdDateTime = LocalDateTime.of(currentDate, currentTime);
        updatedDateTime = LocalDateTime.of(currentDate, currentTime);
    }

    @Nested
    @DisplayName("Get all locations")
    public class getAllLocations {
        @Test
        @DisplayName("Normal")
        public void getLocationsNormal() {
            Location locationTKO = new Location(1, "TKO", createdDateTime, updatedDateTime);
            Location locationCWB = new Location(2, "CWB", createdDateTime, updatedDateTime);
            List<Location> locationList = new ArrayList<Location>();
            locationList.add(locationTKO);
            locationList.add(locationCWB);
            Mockito.when(locationService.getLocations()).thenReturn(locationList);
            ResponseEntity<Response> responseEntity = locationController.getAllLocations();
            List<Location> actualLocationList = (List<Location>)responseEntity.getBody().getMessage();
            Mockito.verify(locationService, Mockito.times(1)).getLocations();
            assertEquals(2, actualLocationList.size());
            assertEquals(1, actualLocationList.get(0).getId());
            assertEquals("TKO", actualLocationList.get(0).getName());
            assertEquals(2, actualLocationList.get(1).getId());
            assertEquals("CWB", actualLocationList.get(1).getName());
            assertEquals(200, responseEntity.getStatusCodeValue());
            assertEquals(true, responseEntity.getBody().isSuccess());
        }

        @Test
        @DisplayName("No Location in Database")
        public void getLocationsNoLocationInDB() {
            List<Location> locationList = new ArrayList<Location>();
            Mockito.when(locationService.getLocations()).thenReturn(locationList);
            ResponseEntity<Response> responseEntity = locationController.getAllLocations();
            List<Location> actualLocationList = (List<Location>)responseEntity.getBody().getMessage();
            Mockito.verify(locationService, Mockito.times(1)).getLocations();
            assertEquals(0, actualLocationList.size());
            assertEquals(200, responseEntity.getStatusCodeValue());
            assertEquals(true, responseEntity.getBody().isSuccess());
        }
    }
}
