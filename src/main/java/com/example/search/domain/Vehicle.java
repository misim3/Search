package com.example.search.domain;

import lombok.Builder;
import lombok.Data;

@Data
public class Vehicle {

    private String type;

    private String id;

    private Double latitude;

    private Double longitude;

    @Builder
    public Vehicle(String type, String id, Double latitude, Double longitude) {
        this.type = type;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
