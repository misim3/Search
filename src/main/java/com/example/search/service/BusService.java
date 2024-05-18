package com.example.search.service;

import com.example.search.domain.Vehicle;
import com.example.search.domain.publicTransit.bus.BGraph;
import com.example.search.domain.publicTransit.bus.BNodeQ;
import com.example.search.domain.publicTransit.bus.Bus;
import com.example.search.domain.publicTransit.bus.Route;
import com.example.search.entity.BusRoute;
import com.example.search.repository.BusRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BusService {

    private final BusRouteRepository busRouteRepository;

    private final SearchWalking searchWalking;

    // 도착 가능한 지점 탐색만
    public List<Vehicle> search(BGraph bGraph, Vehicle start, double timeLimit) {
        double startTime;
        Bus currentNode;
        String previousRouteId;

        Set<String> reachableStops = new HashSet<>();
        PriorityQueue<BNodeQ> currentQueue = new PriorityQueue<>();
        Set<String> visitedStops = new HashSet<>();

        Map<String, Double> solution = new HashMap<>(bGraph.getBusStops().size());

        // 시작점의 정보를 입력
        List<Vehicle> initNode = searchWalking.searchOneToDB(start);

        initNode.forEach(i -> solution.put(i.getId(), searchWalking.getWalkingTime(start, i)));

        initNode.forEach(i -> currentQueue.add(new BNodeQ(bGraph.getBusStops().get(i.getId()), solution.get(i.getId()), null)));

        initNode.forEach(i -> visitedStops.add(i.getId()));

        BNodeQ ndq = null;

        while (!currentQueue.isEmpty()) {
            BNodeQ currentNodeQ = currentQueue.poll();
            currentNode = currentNodeQ.getBus();
            startTime = currentNodeQ.getTime();
            previousRouteId = currentNodeQ.getRouteId();

            if (startTime >= timeLimit) {
                continue;
            }

            if (bGraph.getBusStops().containsKey(currentNode.getNodeId())) {
                for (Route route : currentNode.getRoutes().values()) {
                    double endTime; // 버스 이동 속도에 따라 적절히 조정되어야 함
                    String currentRouteId = route.getRouteId();

                    String currentNodeOrder = currentNode.getRoutes().get(currentRouteId).getNodeOrd();

                    for (Route nextRoute : bGraph.getRoutes().get(currentRouteId).values()) {
                        if (Integer.parseInt(nextRoute.getNodeOrd()) + 1 == Integer.parseInt(currentNodeOrder) || Integer.parseInt(nextRoute.getNodeOrd()) - 1 == Integer.parseInt(currentNodeOrder)) {
                            if (previousRouteId != null) {
                                if (currentRouteId.equals(previousRouteId)) {
                                    endTime = startTime + (60);
                                } else {
                                    endTime = startTime + 60 * 4;
                                }
                            } else {
                                endTime = startTime + 60;
                            }

                            if (endTime < timeLimit && !visitedStops.contains(nextRoute.getNodeId())) {
                                if (solution.containsKey(nextRoute.getNodeId())) {
                                    if (solution.get(nextRoute.getNodeId()) > endTime) {
                                        solution.put(nextRoute.getNodeId(), endTime);
                                    }
                                } else {
                                    solution.put(nextRoute.getNodeId(), endTime);
                                }

                                // 도착할 수 있는 정류장 추가
                                reachableStops.add(nextRoute.getNodeId());

                                ndq = new BNodeQ(bGraph.getBusStops().get(nextRoute.getNodeId()), solution.get(nextRoute.getNodeId()), nextRoute.getNodeId());
                                currentQueue.add(ndq);
                                visitedStops.add(nextRoute.getNodeId());
                            }
                        }
                    }
                }
            }
        }

        return reachableStops.stream()
                .map(r -> Vehicle.builder().build())
                .toList();
    }

    public BGraph makeGraph() {

        BGraph graph = new BGraph();

        List<BusRoute> busRouteList = busRouteRepository.findAll();

        for (BusRoute busRoute : busRouteList) {
            Bus bus;

            if (!graph.getBusStops().containsKey(busRoute.getNodeId())) {
                bus = Bus.builder()
                        .nodeId(busRoute.getNodeId())
                        .build();
            } else {
                bus = graph.getBusStops().get(busRoute.getNodeId());
            }

            Route route = Route.builder()
                    .routeId(busRoute.getRouteId())
                    .nodeId(busRoute.getNodeId())
                    .nodeOrd(busRoute.getNodeOrd())
                    .build();

            graph.addRoute(route);

            bus.addRoutes(route);

            graph.addBus(bus);
        }

        return graph;
    }
}