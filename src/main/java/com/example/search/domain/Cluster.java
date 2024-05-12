package com.example.search.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Cluster {

    private Point medoid;
    private List<Point> points;

    public Cluster(Point medoid) {
        this.medoid = medoid;
        this.points = new ArrayList<Point>();
    }

    public void clearPoints() {
        points.clear();
    }
}
