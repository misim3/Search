package com.example.search.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SLink {

    private String targetSubwayId;

    private String targetSubwayRouteName;

    private double time;

    @Builder
    public SLink(String targetSubwayId, String targetSubwayRouteName, double time) {
        this.targetSubwayId = targetSubwayId;
        this.targetSubwayRouteName = targetSubwayRouteName;
        this.time = time;
    }
}
