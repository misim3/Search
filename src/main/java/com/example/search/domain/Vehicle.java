package com.example.search.domain;

import lombok.Builder;
import lombok.Data;

@Data
public class Vehicle {

    private String type;

    private double latitude;

    private double longitude;

    @Builder
    public Vehicle(String type, double latitude, double longitude) {
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
