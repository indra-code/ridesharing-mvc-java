package com.ridesharing.service;

import com.ridesharing.model.Driver;
import com.ridesharing.model.Ride;
import com.ridesharing.model.RideStatus;
import com.ridesharing.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RideMatchingService {

    private DriverService driverService;
    private RideRepository rideRepository;

    @Autowired
    public RideMatchingService(DriverService driverService, RideRepository rideRepository) {
        this.driverService = driverService;
        this.rideRepository = rideRepository;
    }

    // Simple matching: find the first available driver (improve later)
    public void findDriverForRide(Ride ride) {
        if (ride.getStatus() != RideStatus.REQUESTED) {
            return; // Already matched or cancelled
        }

        List<Driver> availableDrivers = driverService.findAvailableDrivers();

        // Basic strategy: assign the first available driver
        // TODO: Implement more sophisticated matching (location, rating, etc.)
        Optional<Driver> assignedDriver = availableDrivers.stream().findFirst();

        assignedDriver.ifPresent(driver -> {
            // Attempt to accept the ride (could fail if driver becomes unavailable)
            try {
                driverService.acceptRide(driver.getId(), ride.getId());
                System.out.println("Assigned Driver " + driver.getId() + " to Ride " + ride.getId());
                // Notify Rider/Driver (e.g., via WebSockets or Push Notifications)
            } catch (Exception e) {
                System.err.println("Failed to assign driver " + driver.getId() + " to ride " + ride.getId() + ": " + e.getMessage());
                // Potentially try next driver or handle failure
            }
        });

        if (assignedDriver.isEmpty()) {
            System.out.println("No available drivers for Ride " + ride.getId());
            // Handle no available drivers (e.g., notify rider, retry later)
        }
    }
} 