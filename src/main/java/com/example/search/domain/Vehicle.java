package com.example.search.domain;

import lombok.Builder;
import lombok.Data;

@Data
public class Vehicle {

    private String type;

    private String id;

    private double latitude;

    private double longitude;

    @Builder
    public Vehicle(String type, String id, double latitude, double longitude) {
        this.type = type;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
