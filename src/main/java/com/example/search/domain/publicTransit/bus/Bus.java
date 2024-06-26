package com.example.search.domain.publicTransit.bus;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public class Bus {

    private String nodeId;

    private Double latitude;
    private Double longitude;

    private Map<String, Route> routes;

    @Builder
    public Bus(String nodeId, Double longitude, Double latitude) {
        this.nodeId = nodeId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.routes = new HashMap<>();
    }

    public void addRoutes(Route route) {
        routes.put(route.getRouteId(), route);
    }
}
