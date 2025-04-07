package com.ridesharing.service; // Or algorithm package

import com.ridesharing.model.Location;
import com.ridesharing.model.Ride;
import org.springframework.stereotype.Service;

@Service
public class FareCalculationService {

    // Simple fare calculation (improve later)
    public double calculateFare(Ride ride) {
        // TODO: Implement actual fare calculation based on distance, time, demand, etc.
        // For now, a placeholder value
        double baseFare = 5.0;
        double distance = calculateDistance(ride.getStartLocation(), ride.getEndLocation()); // Placeholder
        double time = calculateDuration(ride.getStartTime(), ride.getEndTime()); // Placeholder

        return baseFare + (distance * 1.5) + (time * 0.2);
    }

    private double calculateDistance(Location start, Location end) {
        // Use Haversine formula to calculate the great circle distance between two points
        if (start == null || end == null) {
            return 10.0; // Default fallback value in kilometers
        }
        
        double earthRadius = 6371; // km
        double lat1 = Math.toRadians(start.getLatitude());
        double lon1 = Math.toRadians(start.getLongitude());
        double lat2 = Math.toRadians(end.getLatitude());
        double lon2 = Math.toRadians(end.getLongitude());
        
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        
        return earthRadius * c;
    }

    private double calculateDuration(java.time.LocalDateTime start, java.time.LocalDateTime end) {
        // Placeholder
        if (start == null || end == null) return 0.0;
        return java.time.Duration.between(start, end).toMinutes();
    }
} 