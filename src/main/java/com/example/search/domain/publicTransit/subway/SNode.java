package com.example.search.domain.publicTransit.subway;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class SNode {

    private String subwayId;

    private String subwayRouteName;

    private Double latitude;
    private Double longitude;

    List<SLink> adj;

    @Builder
    public SNode(String subwayId, String subwayRouteName, Double latitude, Double longitude) {
        this.subwayId = subwayId;
        this.subwayRouteName = subwayRouteName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.adj = new ArrayList<>();
    }

    public void addLink(SLink sLink) {
        this.adj.add(sLink);
    }
}
