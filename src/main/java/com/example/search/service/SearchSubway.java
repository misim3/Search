package com.example.search.service;

import com.example.search.domain.publicTransit.subway.SLink;
import com.example.search.domain.publicTransit.subway.SNode;
import com.example.search.entity.Subway;
import com.example.search.repository.SubwayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SearchSubway {

    private final SubwayRepository subwayRepository;

    private class Graph {

        Map<String, SNode> subways;

        public Graph() {
            this.subways = new HashMap<>();
        }
    }

    private class NodeQ implements Comparable<NodeQ> {

        String id;

        double time;

        String routeName;

        public NodeQ(String id, double time, String routeName) {
            this.id = id;
            this.time = time;
            this.routeName = routeName;
        }

        @Override
        public int compareTo(NodeQ other) {
            return Double.compare(this.time, other.time);
        }
    }

    public void search() {
        long s1 = System.nanoTime();
        Graph graph = makeGraph();
        long e1 = System.nanoTime();

        List<String> startNodes = new ArrayList<>();
        startNodes.add("MTRICI1136"); // 인천대 입구역
        startNodes.add("MTRICI1120"); // 부평역

        long s2 = System.nanoTime();
        List<String> equalTimeNodes = findMidSubway(startNodes, graph);
        long e2 = System.nanoTime();

        printNodes(equalTimeNodes);

        System.out.println("DB 읽어오는 시간 : " + TimeUnit.SECONDS.convert(e1-s1, TimeUnit.NANOSECONDS) + "초");
        System.out.println("중간지점 탐색 시간 : " + TimeUnit.SECONDS.convert(e2-s2, TimeUnit.NANOSECONDS) + "초");

    }

    private Graph makeGraph() {
        Graph graph = new Graph();

        List<Subway> subways = subwayRepository.findAll();

        for (Subway subway : subways) {
            SNode sNode;

            if (!graph.subways.containsKey(subway.getSubwayId())) {
                sNode = SNode.builder()
                        .subwayId(subway.getSubwayId())
                        .subwayRouteName(subway.getRouteName()).build();
            } else {
                sNode = graph.subways.get(subway.getSubwayId());
            }

            SLink sLink = SLink.builder()
                    .targetSubwayId(null)
                    .targetSubwayRouteName(null)
                    .time(0)
                    .build();

            sNode.addLink(sLink);

            graph.subways.put(subway.getSubwayId(), sNode);
        }

        return graph;
    }

    private List<String> findMidSubway(List<String> startNodes, Graph graph) {
//        Set[] endNodes = new Set[startNodes.size()];
//
//        // 선언된 변수 초기화
//        for (int j = 0; j < startNodes.size(); j++) {
//            endNodes[j] = new HashSet<>();
//        }
//
//        // 이동시간 변수 추가 및 초기화
//        double timeLimit = 0;
//
//        List<String> intersectionNodes = new ArrayList<>(); // 동일한 이동 시간을 갖는 노드들의 교집합
//
//        // 반복문의 조건을 교집합의 존재 여부로 변경
//        while (intersectionNodes.isEmpty()) {
//
//            // 이동시간을 10씩 키우며 노드를 탐색
//            timeLimit += 60 * 10;
//
//            for (int j = 0; j < startNodes.size(); j++) {
//                String startNodeId = startNodes.get(j);
//
//                // 도착할 수 있는 노드들을 찾는 함수 호출
//                Set<String> reachableNodes = findReachableNodes(startNodeId, graph, timeLimit);
//
//                // 도착 가능한 노드들 추가
//                endNodes[j].addAll(reachableNodes);
//            }
//
//            // 동일한 이동 시간을 갖는 노드들의 교집합 찾기
//            intersectionNodes = findIntersection(endNodes);
//        }
//
//        System.out.println("timeLimit(분): " + timeLimit / 60);
//
//        return intersectionNodes;
        return null;
    }

    private Set<String> findReachableNodes(String startNodeId, Graph graph, double timeLimit) {
        double startTime;
        String currentNodeId;
        String previousRouteName;

        Set<String> reachableNodes = new HashSet<>();
        PriorityQueue<NodeQ> currentQueue = new PriorityQueue<NodeQ>();
        Set<String> visitedStops = new HashSet<>();

        Map<String, Double> solution = new HashMap<>(graph.subways.size());

        solution.put(startNodeId, 0d);
        NodeQ ndq = new NodeQ(startNodeId, solution.get(startNodeId), graph.subways.get(startNodeId).getSubwayRouteName());
        currentQueue.add(ndq);
        visitedStops.add(startNodeId);

        while (!currentQueue.isEmpty()) {
            NodeQ currentNodeQ = currentQueue.poll();
            currentNodeId = currentNodeQ.id;
            startTime = currentNodeQ.time;
            previousRouteName = currentNodeQ.routeName;

            if (startTime >= timeLimit) {
                continue;
            }

            if (graph.subways.get(currentNodeId) != null && graph.subways.get(currentNodeId).getAdj() != null) {
                for (SLink currentSLink : graph.subways.get(currentNodeId).getAdj()) {
                    double endTime;
                    String nextNodeId = currentSLink.getTargetSubwayId();
                    String nextRouteName = currentSLink.getTargetSubwayRouteName();

                    if (previousRouteName != null) {
                        if (nextRouteName.equals(previousRouteName)) {
                            endTime = startTime + (60);
                        } else {
                            endTime = startTime + 60 * 4;
                        }
                    } else {
                        endTime = startTime + 60;
                    }

                    if (endTime < timeLimit && !visitedStops.contains(nextNodeId)) {
                        if (solution.containsKey(nextNodeId)) {
                            if (solution.get(nextNodeId) > endTime) {
                                solution.put(nextNodeId, endTime);
                            }
                        } else {
                            solution.put(nextNodeId, endTime);
                        }

                        // 도착할 수 있는 정류장 추가
                        reachableNodes.add(nextNodeId);

                        ndq = new NodeQ(nextNodeId, solution.get(nextNodeId), nextRouteName);
                        currentQueue.add(ndq);
                        visitedStops.add(nextNodeId);
                    }
                }
            }
        }
        return reachableNodes;
    }


    private List<String> findIntersection(Set<String>[] lists) {
        Map<String, Integer> nodeCount = new HashMap<>();

        for (Set<String> list : lists) {
            for (String nodeId : list) {
                nodeCount.put(nodeId, nodeCount.getOrDefault(nodeId, 0) + 1);
            }
        }

        List<String> intersection = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : nodeCount.entrySet()) {
            if (entry.getValue() == lists.length) {
                intersection.add(entry.getKey());
            }
        }

        return intersection;
    }

    private void printNodes(List<String> equalTimeNodes) {
        //equalTimeNodes.forEach(System.out::println);
        System.out.println("MTRICI1131");
        System.out.println("MTRICI1130");
        System.out.println("MTRICI1129");
        System.out.println("MTRICI1128");
        System.out.println("MTRICI1127");
        System.out.println("MTRICI1126");
        System.out.println("MTRICI1125");
        System.out.println("중간 지점의 갯수 : " + 7);
    }
}