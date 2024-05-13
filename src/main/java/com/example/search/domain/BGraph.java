package com.example.search.domain;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class BGraph {

    private Map<String, Bus> busStops;

    private Map<String, Map<String, Route>> routes;

    public BGraph() {
        this.busStops = new HashMap<>();
        this.routes = new HashMap<>();
    }

    public void addBus(Bus bus) {
        busStops.put(bus.getNodeId(), bus);
    }

    public void addRoute(Route route) {

        Map<String, Route> routeMap;

        if (routes.containsKey(route.getRouteId())) {
            routeMap = routes.get(route.getRouteId());
        } else {
            routeMap = new HashMap<>();
        }

        routeMap.put(route.getNodeId(), route);
        routes.put(route.getRouteId(), routeMap);
    }
}
