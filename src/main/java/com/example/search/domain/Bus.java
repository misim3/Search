package com.example.search.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public class Bus {

    private String nodeId;

    private Map<String, Route> routes;

    @Builder
    public Bus(String nodeId) {
        this.nodeId = nodeId;
        this.routes = new HashMap<>();
    }

    public void addRoutes(Route route) {
        routes.put(route.getRouteId(), route);
    }
}
