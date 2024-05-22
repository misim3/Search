package com.example.search.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String startNodeId;

    private String endNodeId;

    private Double time;

    @Setter
    private Double longitude;

    @Setter
    private Double latitude;

    @Builder
    public Car(String startNodeId, String endNodeId, Double time) {
        this.startNodeId = startNodeId;
        this.endNodeId = endNodeId;
        this.time = time;
    }

    @Builder
    public Car(String startNodeId, String endNodeId, Double time, Double longitude, Double latitude) {
        this.startNodeId = startNodeId;
        this.endNodeId = endNodeId;
        this.time = time;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
