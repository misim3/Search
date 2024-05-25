package com.example.search.domain.car;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CNode {

    private String nodeId;
    private Double latitude;
    private Double longitude;

    List<CLink> adj;

    @Builder
    public CNode(String nodeId) {
        this.nodeId = nodeId;
        this.adj = new ArrayList<>();
    }

    @Builder
    public CNode(String nodeId, Double latitude, Double longitude) {
        this.nodeId = nodeId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.adj = new ArrayList<>();
    }

    public void addLink(CLink CLink) {
        this.adj.add(CLink);
    }
}
