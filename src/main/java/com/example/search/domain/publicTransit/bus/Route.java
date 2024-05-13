package com.example.search.domain.publicTransit.bus;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Route {

    private String nodeOrd;

    private String routeId;

    private String nodeId;

    @Builder
    public Route(String nodeOrd, String routeId, String nodeId) {
        this.nodeOrd = nodeOrd;
        this.routeId = routeId;
        this.nodeId = nodeId;
    }
}
