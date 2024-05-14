package com.example.search.service;

import com.example.search.domain.Vehicle;
import com.example.search.domain.publicTransit.PGraph;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicTransitService {

    private final BusService busService;

    private final SubwayService subwayService;

    // 버스, 지하철 통합 탐색 구현
    public List<Vehicle> search(PGraph pGraph, Vehicle vehicle, double timeLimit) {
        List<Vehicle> result = new ArrayList<>();

        // 버스만
        result.addAll(busService.search(pGraph.getBGraph(), vehicle, timeLimit));

        // 지하철만
        result.addAll(subwayService.search(pGraph.getSGraph(), vehicle, timeLimit));

        // 버스 지하철 환승
        result.addAll(transferSearch(pGraph, vehicle, timeLimit));

        return result;
    }

    private List<Vehicle> transferSearch(PGraph pGraph, Vehicle vehicle, double timeLimit) {
        // 매 노드마다 인접한 노드 + 환승 가능한 정류장 혹은 역 탐색
        return null;
    }

    public PGraph makeGraph() {
        return new PGraph(busService.makeGraph(), subwayService.makeGraph());
    }
}
