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
public class BusStop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nodeId;

    private Integer cityCode;

    @Setter
    private Double latitude;

    @Setter
    private Double longitude;

    public BusStop(String nodeId, int cityCode) {
        this.nodeId = nodeId;
        this.cityCode = cityCode;
    }

    @Builder
    public BusStop(String nodeId, int cityCode, Double latitude, Double longitude) {
        this.nodeId = nodeId;
        this.cityCode = cityCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
