package com.example.search.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Point {

    private double x;
    private double y;

    @Builder
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distanceTo(Point other) {
        double dx = x - other.x;
        double dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
