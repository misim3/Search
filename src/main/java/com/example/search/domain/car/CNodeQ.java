package com.example.search.domain.car;

import lombok.Getter;

@Getter
public class CNodeQ implements Comparable<CNodeQ> {

    private String id;

    private double time;

    public CNodeQ(String id, double time) {
        this.id = id;
        this.time = time;
    }

    @Override
    public int compareTo(CNodeQ o) {
        return Double.compare(this.time, o.time);
    }
}
