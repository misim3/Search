package com.example.search.domain.car;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CLink {

    private String targetNodeId;
    private double time;

    @Builder
    public CLink(String targetNodeId, double time) {
        this.targetNodeId = targetNodeId;
        this.time = time;
    }
}
