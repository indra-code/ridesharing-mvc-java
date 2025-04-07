package com.ridesharing.service;

import com.ridesharing.model.Location;
import com.ridesharing.model.Ride;
import com.ridesharing.model.RideStatus;
import com.ridesharing.model.Rider;
import com.ridesharing.repository.LocationRepository;
import com.ridesharing.repository.RideRepository;
import com.ridesharing.repository.RiderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RideService {

    private final RideRepository rideRepository;
    private final RiderRepository riderRepository;
    private final LocationRepository locationRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public RideService(RideRepository rideRepository, 
                      RiderRepository riderRepository,
                      LocationRepository locationRepository,
                      ApplicationEventPublisher eventPublisher) {
        this.rideRepository = rideRepository;
        this.riderRepository = riderRepository;
        this.locationRepository = locationRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Ride requestRide(Long riderId, String startLocationStr, String endLocationStr, 
                            Double startLat, Double startLng, Double endLat, Double endLng) {
        Rider rider = riderRepository.findById(riderId)
                .orElseThrow(() -> new RuntimeException("Rider not found"));

        Location startLocation = createLocation(startLocationStr, startLat, startLng);
        Location endLocation = createLocation(endLocationStr, endLat, endLng);

        Ride newRide = new Ride();
        newRide.setRider(rider);
        newRide.setStartLocation(startLocation);
        newRide.setEndLocation(endLocation);
        newRide.setRequestTime(LocalDateTime.now());
        newRide.setStatus(RideStatus.REQUESTED);

        Ride savedRide = rideRepository.save(newRide);

        // We'll handle matching asynchronously
        System.out.println("New ride requested with ID: " + savedRide.getId());
        
        // Replace direct service call with an event or manual matching for now
        // In a real app, we would use events or a message queue
        System.out.println("Finding a driver for ride: " + savedRide.getId());

        return savedRide;
    }

    private Location createLocation(String displayName, Double lat, Double lng) {
        Location location = new Location();
        location.setDisplayName(displayName);
        
        // Use provided coordinates if available, otherwise use defaults
        if (lat != null && lng != null) {
            location.setLatitude(lat);
            location.setLongitude(lng);
        } else {
            // Default coordinates for demonstration (these are arbitrary)
            location.setLatitude(0.0);
            location.setLongitude(0.0);
        }
        
        location.setAddress(displayName); // Use display name as address for simplicity
        return locationRepository.save(location);
    }

    @Transactional
    public Ride cancelRide(Long rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        // Add logic to check if ride can be cancelled
        if (ride.getStatus() == RideStatus.REQUESTED || ride.getStatus() == RideStatus.ACCEPTED) {
            ride.setStatus(RideStatus.CANCELLED);
            // Notify driver if accepted?
            if (ride.getDriver() != null) {
                // Potentially make driver available again
            }
            return rideRepository.save(ride);
        } else {
            throw new RuntimeException("Cannot cancel ride in current state");
        }
    }

    public Ride findRideById(Long rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));
    }

    public List<Ride> findRidesByStatus(RideStatus status) {
        return rideRepository.findByStatus(status);
    }

    // Add methods for starting, completing rides, calculating fare, etc.
} 