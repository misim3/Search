package com.example.search.domain;

import java.util.List;

public class Point {

    private double x;
    private double y;
    private List<Point> points;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distanceTo(Point other) {
        double dx = x - other.x;
        double dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public Point calculateCentroid() {
        double sumX = 0;
        double sumY = 0;
        for (Point point : points) {
            sumX += point.x;
            sumY += point.y;
        }
        return new Point(sumX / points.size(), sumY / points.size());
    }

    public void clearPoints() {
        points.clear();
    }
}
