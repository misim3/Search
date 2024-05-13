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

    List<SLink> adj;

    @Builder
    public SNode(String subwayId, String subwayRouteName) {
        this.subwayId = subwayId;
        this.subwayRouteName = subwayRouteName;
        this.adj = new ArrayList<>();
    }

    public void addLink(SLink sLink) {
        this.adj.add(sLink);
    }
}
