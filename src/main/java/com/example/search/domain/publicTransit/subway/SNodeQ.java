package com.example.search.domain.publicTransit.subway;

import lombok.Getter;

@Getter
public class SNodeQ implements Comparable<SNodeQ> {

    private String id;

    private double time;

    private String routeName;

    public SNodeQ(String id, double time, String routeName) {
        this.id = id;
        this.time = time;
        this.routeName = routeName;
    }

    @Override
    public int compareTo(SNodeQ o) {
        return Double.compare(this.time, o.time);
    }
}
