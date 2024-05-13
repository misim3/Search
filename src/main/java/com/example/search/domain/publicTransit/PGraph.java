package com.example.search.domain.publicTransit;

import com.example.search.domain.publicTransit.bus.BGraph;
import com.example.search.domain.publicTransit.subway.SGraph;
import lombok.Getter;

@Getter
public class PGraph {

    private BGraph bGraph;

    private SGraph sGraph;

    public PGraph(BGraph bGraph, SGraph sGraph) {
        this.bGraph = bGraph;
        this.sGraph = sGraph;
    }
}
