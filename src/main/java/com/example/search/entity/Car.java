package com.example.search.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String startNodeId;

    private String endNodeId;

    private double time;

    @Builder
    public Car(String startNodeId, String endNodeId, double time) {
        this.startNodeId = startNodeId;
        this.endNodeId = endNodeId;
        this.time = time;
    }
}
