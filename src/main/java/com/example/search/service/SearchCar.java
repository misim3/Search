package com.example.search.service;

import com.example.search.domain.car.CLink;
import com.example.search.domain.car.CNode;
import com.example.search.entity.Car;
import com.example.search.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SearchCar {

    private final CarRepository carRepository;

    private class Graph {
        Map<String, CNode> nodes;

        public Graph() {
            this.nodes = new HashMap<>();
        }
    }

    private class NodeQ implements Comparable<NodeQ> {
        String id;
        double time;

        public NodeQ(String id, double time) {
            this.id = id;
            this.time = time;
        }

        @Override
        public int compareTo(NodeQ other) {
            return Double.compare(this.time, other.time);
        }
    }

    // 도착 가능한 지점 탐색:
    
    public void search() {
        long s1 = System.nanoTime();
        Graph graph = makeGraph();
        long e1 = System.nanoTime();

        List<String> startNodes = new ArrayList<>();
        startNodes.add("1640030400"); // 인천대정문
        startNodes.add("1640030300"); // 센트럴파크역

        long s2 = System.nanoTime();
        List<String> equalTimeNodes = findMidNodes(startNodes, graph);
        long e2 = System.nanoTime();

        printNodes(equalTimeNodes);

        System.out.println("DB 읽어오는 시간 : " + TimeUnit.SECONDS.convert(e1-s1, TimeUnit.NANOSECONDS) + "초");
        System.out.println("중간지점 탐색 시간 : " + TimeUnit.SECONDS.convert(e2-s2, TimeUnit.NANOSECONDS) + "초");
    }

    private Graph makeGraph() {
        Graph graph = new Graph();

        List<Car> carList = carRepository.findAll();

        for (Car car : carList) {
            CNode cNode;

            if (!graph.nodes.containsKey(car.getStartNodeId())) {
                cNode = CNode.builder()
                        .nodeId(car.getStartNodeId())
                        .build();
            } else {
                cNode = graph.nodes.get(car.getStartNodeId());
            }

            CLink cLink = CLink.builder()
                    .targetNodeId(car.getEndNodeId())
                    .time(car.getTime())
                    .build();

            cNode.addLink(cLink);

            graph.nodes.put(car.getStartNodeId(), cNode);
        }

        return graph;
    }

    private void printNodes(List<String> equalTimeNodes) {
        equalTimeNodes.forEach(System.out::println);

        System.out.println("중간 지점의 갯수 : " + equalTimeNodes.size());
    }

    private List<String> findMidNodes(List<String> startNodes, Graph graph) {
        Set[] endNodes = new Set[startNodes.size()];

        // 선언된 변수 초기화
        for (int j = 0; j < startNodes.size(); j++) {
            endNodes[j] = new HashSet<>();
        }

        // 이동시간 변수 추가 및 초기화
        double timeLimit = 0;

        List<String> intersectionNodes = new ArrayList<>(); // 동일한 이동 시간을 갖는 노드들의 교집합

        // 반복문의 조건을 교집합의 존재 여부로 변경
        while (intersectionNodes.isEmpty()) {

            // 이동시간을 10씩 키우며 노드를 탐색
            timeLimit += 60 * 10;

            for (int j = 0; j < startNodes.size(); j++) {
                String startNodeId = startNodes.get(j);

                // 도착할 수 있는 노드들을 찾는 함수 호출
                Set<String> reachableNodes = findReachableNodes(startNodeId, graph, timeLimit);

                // 도착 가능한 노드들 추가
                endNodes[j].addAll(reachableNodes);
            }

            // 동일한 이동 시간을 갖는 노드들의 교집합 찾기
            intersectionNodes = findIntersection(endNodes);
        }

        System.out.println("timeLimit(분): " + timeLimit / 60);

        return intersectionNodes;
    }

    private Set<String> findReachableNodes(String startNodeId, Graph graph, double timeLimit) {
        double startTime;
        String currentNodeId;

        Set<String> reachableNodes = new HashSet<>();
        PriorityQueue<NodeQ> currentQueue = new PriorityQueue<>();
        Set<String> visitedNodes = new HashSet<>();

        Map<String, Double> solution = new HashMap<>(graph.nodes.size());

        // 시작점의 정보를 입력
        solution.put(startNodeId, 0d);
        NodeQ ndq = new NodeQ(startNodeId, solution.get(startNodeId));
        currentQueue.add(ndq);
        visitedNodes.add(startNodeId);

        while (!currentQueue.isEmpty()) {
            NodeQ currentNodeQ = currentQueue.poll();
            currentNodeId = currentNodeQ.id;
            startTime = currentNodeQ.time;

            if (startTime >= timeLimit) {
                continue;
            }

            if (graph.nodes.get(currentNodeId) != null && graph.nodes.get(currentNodeId).getAdj() != null) {
                for (CLink currentCLink : graph.nodes.get(currentNodeId).getAdj()) {
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

                        ndq = new NodeQ(nextNodeId, solution.get(nextNodeId));
                        currentQueue.add(ndq);
                        visitedNodes.add(nextNodeId);
                    }
                }
            }
        }
        return reachableNodes;
    }

    private List<String> findIntersection(Set<String>[] endNodes) {

        Map<String, Integer> nodeCount = new HashMap<>();

        // 각 노드가 나타난 횟수를 세기
        for (Set<String> list : endNodes) {
            for (String nodeId : list) {
                nodeCount.put(nodeId, nodeCount.getOrDefault(nodeId, 0) + 1);
            }
        }

        List<String> intersection = new ArrayList<>();

        // 중복된 노드만 선택
        for (Map.Entry<String, Integer> entry : nodeCount.entrySet()) {
            if (entry.getValue() == endNodes.length) {
                intersection.add(entry.getKey());
            }
        }

        return intersection;
    }
}
