package com.ridesharing.controller;

import com.ridesharing.model.Ride;
import com.ridesharing.service.RideService;
import com.ridesharing.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.ridesharing.model.RideStatus;
import java.util.List;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/ride")
public class RideController {

    @Autowired
    private RideService rideService;

    @Autowired
    private DriverService driverService;

    // Show ride request form (map-based)
    @GetMapping("/request")
    public String showRequestForm(Model model) {
        return "ride-request";
    }

    // Handle ride request with location coordinates
    @PostMapping("/request")
    public String handleRideRequest(@RequestParam Long riderId,
                                  @RequestParam String startLocation,
                                  @RequestParam String endLocation,
                                  @RequestParam(required = false) Double startLat,
                                  @RequestParam(required = false) Double startLng,
                                  @RequestParam(required = false) Double endLat,
                                  @RequestParam(required = false) Double endLng,
                                  Model model) {
        try {
            Ride ride = rideService.requestRide(
                riderId, 
                startLocation, 
                endLocation, 
                startLat, 
                startLng, 
                endLat, 
                endLng
            );
            
            return "redirect:/ride/status/" + ride.getId();
        } catch (Exception e) {
            model.addAttribute("error", "Failed to request ride: " + e.getMessage());
            return "ride-request";
        }
    }
    
    // Show ride status with map
    @GetMapping("/status/{rideId}")
    public String showRideStatus(@PathVariable Long rideId, Model model) {
        try {
            Ride ride = rideService.findRideById(rideId);
            
            // Debug logging
            System.out.println("Ride found: " + ride.getId());
            System.out.println("Start location: " + (ride.getStartLocation() != null ? 
                ride.getStartLocation().getLatitude() + ", " + ride.getStartLocation().getLongitude() : "null"));
            System.out.println("End location: " + (ride.getEndLocation() != null ? 
                ride.getEndLocation().getLatitude() + ", " + ride.getEndLocation().getLongitude() : "null"));
            
            // Ensure we have valid location data
            if (ride.getStartLocation() == null || ride.getEndLocation() == null) {
                model.addAttribute("error", "Missing location data for ride");
                return "redirect:/ride/request";
            }
            
            model.addAttribute("ride", ride);
            return "ride-status";
        } catch (Exception e) {
            System.err.println("Error loading ride: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error loading ride: " + e.getMessage());
            return "redirect:/ride/request";
        }
    }

    // Cancel ride
    @PostMapping("/cancel/{rideId}")
    public String cancelRide(@PathVariable Long rideId, Model model) {
        try {
            Ride ride = rideService.cancelRide(rideId);
            model.addAttribute("message", "Ride cancelled successfully");
            model.addAttribute("ride", ride);
            return "ride-status";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to cancel ride: " + e.getMessage());
            return "redirect:/ride/status/" + rideId;
        }
    }

    // Show available rides to driver
    @GetMapping("/available")
    public String showAvailableRides(HttpSession session, Model model) {
        // Check if driver is logged in
        Long driverId = (Long) session.getAttribute("driverId");
        if (driverId == null) {
            return "redirect:/driver/login";
        }

        try {
            List<Ride> availableRides = rideService.findRidesByStatus(RideStatus.REQUESTED);
            model.addAttribute("rides", availableRides);
            return "available-rides";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading available rides: " + e.getMessage());
            return "redirect:/";
        }
    }

    // Accept ride by driver
    @PostMapping("/accept/{rideId}")
    public String acceptRide(@PathVariable Long rideId,
                           @RequestParam Long driverId,
                           Model model) {
        try {
            Ride ride = driverService.acceptRide(driverId, rideId);
            model.addAttribute("ride", ride);
            // Redirect to driver route page instead of ride status
            return "redirect:/ride/route/" + rideId;
        } catch (Exception e) {
            model.addAttribute("error", "Failed to accept ride: " + e.getMessage());
            return "redirect:/ride/available";
        }
    }

    // Add new endpoint for driver's route view
    @GetMapping("/route/{rideId}")
    public String showDriverRoute(@PathVariable Long rideId, Model model) {
        try {
            Ride ride = rideService.findRideById(rideId);
            if (ride.getDriver() == null) {
                return "redirect:/ride/available";
            }
            model.addAttribute("ride", ride);
            return "driver-route";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading route: " + e.getMessage());
            return "redirect:/ride/available";
        }
    }
} 