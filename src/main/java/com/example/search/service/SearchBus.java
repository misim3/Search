package com.example.search.service;

import com.example.search.domain.publicTransit.bus.Bus;
import com.example.search.domain.publicTransit.bus.Route;
import com.example.search.entity.BusRoute;
import com.example.search.repository.BusRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SearchBus {

    private final BusRouteRepository busRouteRepository;

    private class Graph {

        Map<String, Bus> busStops;

        Map<String, Map<String, Route>> routes;

        public Graph() {
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

    private class NodeQ implements Comparable<NodeQ> {

        Bus bus;

        double time;

        String routeId;

        public NodeQ(String routeId, Bus bus, double time) {
            this.routeId = routeId;
            this.bus = bus;
            this.time = time;
        }

        @Override
        public int compareTo(NodeQ o) {
            return Double.compare(this.time, o.time);
        }
    }
    
    public void search() {
        long s1 = System.nanoTime();
        Graph graph = makeGraph();
        long e1 = System.nanoTime();

        List<String> nodes = new ArrayList<String>();
        //nodes.add("ICB164000396"); // 인천대입구역
        nodes.add("ICB164000377"); // 인천대학교공과대학
        nodes.add("ICB166000891"); // 힘찬병원

        long s2 = System.nanoTime();
        List<String> eqaulTimeBusStops = findMidBusStops(nodes, graph);
        long e2 = System.nanoTime();

        printBusStops(eqaulTimeBusStops);

        System.out.println("DB 읽어오는 시간 : " + TimeUnit.SECONDS.convert(e1-s1, TimeUnit.NANOSECONDS) + "초");
        System.out.println("중간지점 탐색 시간 : " + TimeUnit.SECONDS.convert(e2-s2, TimeUnit.NANOSECONDS) + "초");
    }

    private void printBusStops(List<String> eqaulTimeBusStops) {
        eqaulTimeBusStops.forEach(System.out::println);

        System.out.println("중간 지점의 갯수 : " + eqaulTimeBusStops.size());
    }

    private List<String> findMidBusStops(List<String> nodes, Graph graph) {
        // 각 정류장에서 도달 가능한 정류장들의 집합
        Set[] endStops = new Set[nodes.size()];

        // 초기화
        for (int i = 0; i < nodes.size(); i++) {
            endStops[i] = new HashSet<>();
        }

        // 이동시간 변수 추가 및 초기화
        double timeLimit = 0;

        List<String> intersectionStops = new ArrayList<>(); // 동일한 이동 시간을 갖는 정류장들의 교집합

        // 교집합이 없을 때 반복
        while (intersectionStops.isEmpty()) {
            // 이동시간 증가
            timeLimit += 60 * 10; // 10분 단위로 증가 (단위: 분)


            // 각 정류장에 대해 이동 가능한 정류장 탐색
            for (int i = 0; i < nodes.size(); i++) {
                String startStopId = nodes.get(i);

                // 도착할 수 있는 정류장들을 찾음
                Set<String> reachableStops = findReachableBusStops(startStopId, graph, timeLimit);

                // 도착 가능한 정류장들 추가
                endStops[i].addAll(reachableStops);
            }

            // 동일한 이동 시간을 갖는 정류장들의 교집합 찾기
            intersectionStops = findIntersection(endStops);
        }

        System.out.println("이동 시간 제한: " + timeLimit / 60 + "분");

        return intersectionStops;
    }

    private List<String> findIntersection(Set[] endStops) {
        Map<String, Integer> stopCount = new HashMap<>();

        // 각 정류장이 나타난 횟수 세기
        for (Set<String> list : endStops) {
            for (String stopId : list) {
                stopCount.put(stopId, stopCount.getOrDefault(stopId, 0) + 1);
            }
        }

        List<String> intersectionStops = new ArrayList<>();

        // 중복된 정류장만 선택
        for (Map.Entry<String, Integer> entry : stopCount.entrySet()) {
            if (entry.getValue() == endStops.length) {
                intersectionStops.add(entry.getKey()); // 실제 위도, 경도는 데이터에서 가져와야 함
            }
        }

        return intersectionStops;
    }

    private Set<String> findReachableBusStops(String startStopId, Graph graph, double timeLimit) {
        double startTime;
        Bus currentNode;
        String previousRouteId;

        Set<String> reachableStops = new HashSet<>();
        PriorityQueue<NodeQ> currentQueue = new PriorityQueue<>();
        Set<String> visitedStops = new HashSet<>();

        Map<String, Double> solution = new HashMap<>(graph.busStops.size());

        // 시작점의 정보를 입력
        solution.put(startStopId, 0d);
        NodeQ ndq = new NodeQ(null, graph.busStops.get(startStopId), solution.get(startStopId));
        currentQueue.add(ndq);
        visitedStops.add(startStopId);

        while (!currentQueue.isEmpty()) {
            NodeQ currentNodeQ = currentQueue.poll();
            currentNode = currentNodeQ.bus;
            startTime = currentNodeQ.time;
            previousRouteId = currentNodeQ.routeId;

            if (startTime >= timeLimit) {
                continue;
            }

            if (graph.busStops.containsKey(currentNode.getNodeId())) {
                for (Route route : currentNode.getRoutes().values()) {
                    double endTime; // 버스 이동 속도에 따라 적절히 조정되어야 함
                    String currentRouteId = route.getRouteId();

                    String currentNodeOrder = currentNode.getRoutes().get(currentRouteId).getNodeOrd();

                    for (Route nextRoute : graph.routes.get(currentRouteId).values()) {
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

                                ndq = new NodeQ(nextRoute.getNodeId(), graph.busStops.get(nextRoute.getNodeId())
                                        , solution.get(nextRoute.getNodeId()));
                                currentQueue.add(ndq);
                                visitedStops.add(nextRoute.getNodeId());
                            }
                        }
                    }
                }
            }
        }
        return reachableStops;
    }

    private Graph makeGraph() {

        Graph graph = new Graph();

        List<BusRoute> busRouteList = busRouteRepository.findAll();

        for (BusRoute busRoute : busRouteList) {
            Bus bus;

            if (!graph.busStops.containsKey(busRoute.getNodeId())) {
                bus = Bus.builder()
                        .nodeId(busRoute.getNodeId())
                        .build();
            } else {
                bus = graph.busStops.get(busRoute.getNodeId());
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
