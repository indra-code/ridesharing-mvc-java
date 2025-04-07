package com.ridesharing.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rider_id")
    private Rider rider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver; // Nullable until a driver accepts

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "start_location_id")
    private Location startLocation;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "end_location_id")
    private Location endLocation;

    @Column(nullable = false)
    private LocalDateTime requestTime;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RideStatus status;

    private Double fare;
    
    // For backwards compatibility
    @Transient
    private String startLocationString;
    
    @Transient
    private String endLocationString;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Rider getRider() {
        return rider;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }
    
    public String getStartLocationString() {
        return startLocation != null ? startLocation.getDisplayName() : startLocationString;
    }

    public void setStartLocationString(String startLocationString) {
        this.startLocationString = startLocationString;
    }

    public String getEndLocationString() {
        return endLocation != null ? endLocation.getDisplayName() : endLocationString;
    }

    public void setEndLocationString(String endLocationString) {
        this.endLocationString = endLocationString;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }

    public Double getFare() {
        return fare;
    }

    public void setFare(Double fare) {
        this.fare = fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ride ride = (Ride) o;
        return Objects.equals(id, ride.id) && Objects.equals(rider, ride.rider) && Objects.equals(driver, ride.driver) && Objects.equals(requestTime, ride.requestTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rider, driver, requestTime);
    }
} 