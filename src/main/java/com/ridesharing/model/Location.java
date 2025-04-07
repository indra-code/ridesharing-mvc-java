package com.ridesharing.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String displayName;
    private double latitude;
    private double longitude;
    private String address;

    public Location() {
    }

    public Location(String displayName, double latitude, double longitude, String address) {
        this.displayName = displayName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Double.compare(location.latitude, latitude) == 0 &&
               Double.compare(location.longitude, longitude) == 0 &&
               Objects.equals(id, location.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, latitude, longitude);
    }
} 