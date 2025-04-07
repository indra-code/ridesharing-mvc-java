package com.ridesharing.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Rider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    // Location would be more complex in a real app (e.g., separate entity, lat/lon)
    private String currentLocation;

    @OneToMany(mappedBy = "rider", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ride> rides = new ArrayList<>();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public List<Ride> getRides() {
        return rides;
    }

    public void setRides(List<Ride> rides) {
        this.rides = rides;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rider rider = (Rider) o;
        return Objects.equals(id, rider.id) && Objects.equals(user, rider.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user);
    }
} 