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
public class BusRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer cityCode;

    private String nodeId;

    private String nodeOrd;

    private String routeId;

    @Builder
    public BusRoute(Integer cityCode, String nodeId, String nodeOrd, String routeId) {
        this.cityCode = cityCode;
        this.nodeId = nodeId;
        this.nodeOrd = nodeOrd;
        this.routeId = routeId;
    }
}
