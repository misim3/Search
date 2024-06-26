package com.example.search.service;

import com.example.search.domain.Vehicle;
import com.example.search.domain.publicTransit.subway.SGraph;
import com.example.search.domain.publicTransit.subway.SLink;
import com.example.search.domain.publicTransit.subway.SNode;
import com.example.search.domain.publicTransit.subway.SNodeQ;
import com.example.search.entity.Subway;
import com.example.search.repository.SubwayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SubwayService {

    private final SubwayRepository subwayRepository;

    private final SearchWalking searchWalking;

    // 도착 가능한 지점 탐색만
    public List<Vehicle> search(SGraph sGraph, Vehicle start, double timeLimit) {
        double startTime;
        String currentNodeId;
        String previousRouteName;

        Set<String> reachableNodes = new HashSet<>();
        PriorityQueue<SNodeQ> currentQueue = new PriorityQueue<SNodeQ>();
        Set<String> visitedStops = new HashSet<>();

        Map<String, Double> solution = new HashMap<>(sGraph.getSubways().size());

        // 시작점의 정보를 입력
        List<Vehicle> initNode = searchWalking.searchOneToDB(start);

        initNode.forEach(i -> solution.put(i.getId(), searchWalking.getWalkingTime(start, i)));

        initNode.forEach(i -> currentQueue.add(new SNodeQ(i.getId(), solution.get(i.getId()), sGraph.getSubways().get(i.getId()).getSubwayRouteName())));

        initNode.forEach(i -> visitedStops.add(i.getId()));

        SNodeQ ndq = null;

        while (!currentQueue.isEmpty()) {
            SNodeQ currentNodeQ = currentQueue.poll();
            currentNodeId = currentNodeQ.getId();
            startTime = currentNodeQ.getTime();
            previousRouteName = currentNodeQ.getRouteName();

            if (startTime >= timeLimit) {
                continue;
            }

            if (sGraph.getSubways().get(currentNodeId) != null && sGraph.getSubways().get(currentNodeId).getAdj() != null) {
                for (SLink currentSLink : sGraph.getSubways().get(currentNodeId).getAdj()) {
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

                        ndq = new SNodeQ(nextNodeId, solution.get(nextNodeId), nextRouteName);
                        currentQueue.add(ndq);
                        visitedStops.add(nextNodeId);
                    }
                }
            }
        }

        return reachableNodes.stream()
                .map(r -> Vehicle.builder()
                        .longitude(sGraph.getSubways().get(r).getLongitude())
                        .latitude(sGraph.getSubways().get(r).getLatitude())
                        .build())
                .toList();
    }

    public SGraph makeGraph() {
        SGraph graph = new SGraph();

        List<Subway> subways = subwayRepository.findAll();

        for (Subway subway : subways) {
            SNode sNode;

            if (!graph.getSubways().containsKey(subway.getSubwayId())) {
                sNode = SNode.builder()
                        .subwayId(subway.getSubwayId())
                        .subwayRouteName(subway.getRouteName()).build();
            } else {
                sNode = graph.getSubways().get(subway.getSubwayId());
            }

            SLink sLink = SLink.builder()
                    .targetSubwayId(null)
                    .targetSubwayRouteName(null)
                    .time(0)
                    .build();

            sNode.addLink(sLink);

            graph.getSubways().put(subway.getSubwayId(), sNode);
        }

        return graph;
    }
}