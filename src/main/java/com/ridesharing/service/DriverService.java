package com.ridesharing.service;

import com.ridesharing.model.Driver;
import com.ridesharing.model.Ride;
import com.ridesharing.model.RideStatus;
import com.ridesharing.repository.DriverRepository;
import com.ridesharing.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private RideRepository rideRepository;

    // Method to find available drivers (simplified)
    public List<Driver> findAvailableDrivers() {
        return driverRepository.findByAvailableTrue();
    }

    public Optional<Driver> findById(Long id) {
        return driverRepository.findById(id);
    }

    public void setDriverAvailability(Long driverId, boolean available) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        driver.setAvailable(available);
        driverRepository.save(driver);
    }

    public Ride acceptRide(Long driverId, Long rideId) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        if (ride.getStatus() != RideStatus.REQUESTED || !driver.isAvailable()) {
            throw new RuntimeException("Cannot accept ride");
        }

        ride.setDriver(driver);
        ride.setStatus(RideStatus.ACCEPTED);
        driver.setAvailable(false); // Driver is no longer available

        driverRepository.save(driver);
        return rideRepository.save(ride);
    }

    // Add methods for completing rides, updating location, etc.
} 