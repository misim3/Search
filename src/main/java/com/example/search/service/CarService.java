package com.example.search.service;

import com.example.search.domain.Vehicle;
import com.example.search.domain.car.CGraph;
import com.example.search.domain.car.CLink;
import com.example.search.domain.car.CNode;
import com.example.search.domain.car.CNodeQ;
import com.example.search.entity.Car;
import com.example.search.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    private final SearchWalking searchWalking;

    // 도착 가능한 지점 탐색만
    public List<Vehicle> search(CGraph cGraph, Vehicle start, double timeLimit) {
        double startTime;
        String currentNodeId;

        Set<String> reachableNodes = new HashSet<>();
        PriorityQueue<CNodeQ> currentQueue = new PriorityQueue<>();
        Set<String> visitedNodes = new HashSet<>();

        Map<String, Double> solution = new HashMap<>(cGraph.getNodes().size());

        // 시작점의 정보를 입력
        List<Vehicle> initNode = searchWalking.searchOneToDB(start);

        solution.put(startNodeId, 0d);
        CNodeQ ndq = new CNodeQ(startNodeId, solution.get(startNodeId));
        currentQueue.add(ndq);
        visitedNodes.add(startNodeId);

        while (!currentQueue.isEmpty()) {
            CNodeQ currentNodeQ = currentQueue.poll();
            currentNodeId = currentNodeQ.getId();
            startTime = currentNodeQ.getTime();

            if (startTime >= timeLimit) {
                continue;
            }

            if (cGraph.getNodes().get(currentNodeId) != null && cGraph.getNodes().get(currentNodeId).getAdj() != null) {
                for (CLink currentCLink : cGraph.getNodes().get(currentNodeId).getAdj()) {
                    double endTime = startTime + currentCLink.getTime();
                    String nextNodeId = currentCLink.getTargetNodeId();

                    if (endTime < timeLimit && !visitedNodes.contains(nextNodeId)) {
                        if (solution.containsKey(nextNodeId)) {
                            if (solution.get(nextNodeId) > endTime) {
                                solution.put(nextNodeId, endTime);
                            }
                        } else {
                            solution.put(nextNodeId, endTime);
                        }

                        // 도착할 수 있는 노드 추가
                        reachableNodes.add(nextNodeId);

                        ndq = new CNodeQ(nextNodeId, solution.get(nextNodeId));
                        currentQueue.add(ndq);
                        visitedNodes.add(nextNodeId);
                    }
                }
            }
        }

        return reachableNodes.stream()
                .map(r -> Vehicle.builder().build())
                .toList();
    }

    public CGraph makeGraph() {
        CGraph graph = new CGraph();

        List<Car> carList = carRepository.findAll();

        for (Car car : carList) {
            CNode cNode;

            if (!graph.getNodes().containsKey(car.getStartNodeId())) {
                cNode = CNode.builder()
                        .nodeId(car.getStartNodeId())
                        .build();
            } else {
                cNode = graph.getNodes().get(car.getStartNodeId());
            }

            CLink cLink = CLink.builder()
                    .targetNodeId(car.getEndNodeId())
                    .time(car.getTime())
                    .build();

            cNode.addLink(cLink);

            graph.getNodes().put(car.getStartNodeId(), cNode);
        }

        return graph;
    }
}
