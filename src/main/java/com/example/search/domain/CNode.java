package com.example.search.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CNode {

    private String nodeId;
    private double latitude;
    private double longitude;

    List<CLink> adj;

    @Builder
    public CNode(String nodeId) {
        this.nodeId = nodeId;
        this.adj = new ArrayList<>();
    }

    @Builder
    public CNode(String nodeId, double latitude, double longitude) {
        this.nodeId = nodeId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.adj = new ArrayList<>();
    }

    public void addLink(CLink CLink) {
        this.adj.add(CLink);
    }
}
