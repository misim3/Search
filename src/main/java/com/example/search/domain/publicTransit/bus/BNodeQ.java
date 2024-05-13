package com.example.search.domain.publicTransit.bus;

import lombok.Getter;

@Getter
public class BNodeQ implements Comparable<BNodeQ> {

    private Bus bus;

    private double time;

    private String routeId;

    public BNodeQ(Bus bus, double time, String routeId) {
        this.bus = bus;
        this.time = time;
        this.routeId = routeId;
    }

    @Override
    public int compareTo(BNodeQ o) {
        return Double.compare(this.time, o.time);
    }
}
