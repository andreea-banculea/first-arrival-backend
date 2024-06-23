package com.app.first_arrival.service;

import com.app.first_arrival.entities.Emergency;
import com.app.first_arrival.entities.Location;
import com.app.first_arrival.entities.enums.EmergencyStatus;
import com.app.first_arrival.repository.EmergencyRepository;
import com.app.first_arrival.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class LocationService {

    private static final Logger logger = Logger.getLogger(LocationService.class.getName());

    private final LocationRepository locationRepository;

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
//    public Map<Long, Location> findAllLocationsForActiveEmergencies() {
//        List<Emergency> emergencies = emergencyRepository.findAllByStatusIn(
//                List.of(EmergencyStatus.PENDING, EmergencyStatus.ACCEPTED)
//        );
//        Map<Long, Location> locationMap = new HashMap<>();
//        emergencies.stream().filter(emergency -> emergency.getLocation() != null).forEach(emergency -> locationMap.put(emergency.getId(), emergency.getLocation()));
//        return locationMap;
//    }
//

}
