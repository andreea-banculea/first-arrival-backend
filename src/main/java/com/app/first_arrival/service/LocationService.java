package com.app.first_arrival.service;

import com.app.first_arrival.entities.Location;
import com.app.first_arrival.entities.User;
import com.app.first_arrival.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class LocationService {

    private static final Logger logger = Logger.getLogger(LocationService.class.getName());

    private final LocationRepository locationRepository;

    private static final double EARTH_RADIUS = 6371;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    public Optional<Location> findById(Integer id) {
        return locationRepository.findById(id);
    }

    public Location save(Location location) {
        logger.info("Saving Location: " + location);
        return locationRepository.save(location);
    }

    public void deleteById(Integer id) {
        locationRepository.deleteById(id);
    }

    public Location update(Location location) {
        return locationRepository.save(location);
    }

    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    public List<User> getUsersWithinRadius(double lat, double lon, double radius, List<User> users) {
        List<User> nearbyUsers = new ArrayList<>();
        for (User user : users) {
            double distance = calculateDistance(lat, lon, user.getLocation().getLatitude(), user.getLocation().getLongitude());
            if (distance <= radius) {
                nearbyUsers.add(user);
            }
        }
        return nearbyUsers;
    }

}
