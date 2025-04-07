package com.ridesharing.config;

import com.ridesharing.model.*;
import com.ridesharing.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private LocationRepository locationRepository;

    @Override
    @Transactional
    public void run(String... args) {
        // Create test data if the database is empty
        if (userRepository.count() == 0) {
            loadTestData();
        }
    }

    @Transactional
    private void loadTestData() {
        // Create test users
        User riderUser = new User();
        riderUser.setUsername("testrider");
        riderUser.setEmail("rider@example.com");
        riderUser.setPassword("password"); // In real app, this would be hashed
        riderUser.setRole(UserRole.RIDER);
        riderUser = userRepository.save(riderUser);

        User driverUser = new User();
        driverUser.setUsername("testdriver");
        driverUser.setEmail("driver@example.com");
        driverUser.setPassword("password"); // In real app, this would be hashed
        driverUser.setRole(UserRole.DRIVER);
        driverUser = userRepository.save(driverUser);

        // Create sample locations
        Location downtownLocation = createLocation("Downtown", 40.7128, -74.0060, "Downtown area");
        Location airportLocation = createLocation("Airport", 40.6413, -73.7781, "JFK Airport");
        Location mallLocation = createLocation("Shopping Mall", 40.7580, -73.9855, "Central Mall");
        
        // Create a vehicle first
        Vehicle vehicle = new Vehicle();
        vehicle.setMake("Toyota");
        vehicle.setModel("Camry");
        vehicle.setLicensePlate("TEST123");
        vehicle = vehicleRepository.save(vehicle);

        // Create a rider
        Rider rider = new Rider();
        rider.setUser(riderUser);
        rider.setCurrentLocation("Test Rider Location");
        rider = riderRepository.save(rider);

        // Create a driver
        Driver driver = new Driver();
        driver.setUser(driverUser);
        driver.setVehicle(vehicle);
        driver.setAvailable(true);
        driver.setCurrentLocation("Test Driver Location");
        driver = driverRepository.save(driver);

        // Update the vehicle with the driver reference
        vehicle.setDriver(driver);
        vehicleRepository.save(vehicle);

        System.out.println("Test data loaded successfully!");
        System.out.println("Created Rider ID: " + rider.getId());
        System.out.println("Created Driver ID: " + driver.getId());
        System.out.println("Created sample locations: Downtown, Airport, Shopping Mall");
    }
    
    private Location createLocation(String name, double lat, double lng, String address) {
        Location location = new Location();
        location.setDisplayName(name);
        location.setLatitude(lat);
        location.setLongitude(lng);
        location.setAddress(address);
        return locationRepository.save(location);
    }
} 