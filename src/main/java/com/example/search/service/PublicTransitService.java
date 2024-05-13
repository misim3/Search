package com.example.search.service;

import com.example.search.domain.publicTransit.PGraph;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublicTransitService {

    private final BusService busService;

    private final SubwayService subwayService;

    // 버스, 지하철 통합 탐색 구현
    public void search() {
        // 버스만
        
        // 지하철만
        
        // 버스 지하철 환승
        
    }

    public PGraph makeGraph() {
        return new PGraph(busService.makeGraph(), subwayService.makeGraph());
    }
}
