package com.app.first_arrival.controller;

import com.app.first_arrival.entities.Location;
import com.app.first_arrival.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private static final Logger logger = Logger.getLogger(LocationController.class.getName());

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }


    @GetMapping
    public List<Location> getAllLocations() {
        return locationService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Integer id) {
        return locationService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public Location createLocation(@RequestBody Location location) {
        logger.info("Received Location: " + location);
        return locationService.save(location);
    }

    @PutMapping
    public Location updateLocation(@RequestBody Location location) {
        return locationService.update(location);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Integer id) {
        locationService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
