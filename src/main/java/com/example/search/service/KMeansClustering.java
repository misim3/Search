package com.example.search.service;

import com.example.search.domain.Point;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KMeansClustering {

    public List<Point> cluster(int k, List<Point> points, int maxIterations) {

        List<Point> centroids = initializeCentroids(k, points);

        for (int i = 0; i < maxIterations; i++) {
            // 할당
            assginPointsToCentroids(centroids, points);

            // 업데이트 단계
            List<Point> newCentroids = calculateNewCentroids(centroids, points);
            if (centroids.equals(newCentroids)) {
                // 중심점이 더 이상 이동하지 않으면 종료
                break;
            }

            centroids = newCentroids;
        }

        return centroids;
    }

    private List<Point> calculateNewCentroids(List<Point> centroids, List<Point> points) {
        List<Point> newCentroids = new ArrayList<>();
        for (Point centroid : centroids) {
            newCentroids.add(centroid.calculateCentroid());
        }
        return newCentroids;
    }

    private void assginPointsToCentroids(List<Point> centroids, List<Point> points) {

        // 기존 중심점의 클러스터 초기화
        for (Point centroid : centroids) {
            centroid.clearPoints();
        }

        for (Point point : points) {
            double minDistance = Double.MAX_VALUE;
            Point closestCentroid = null;
            for (Point centroid : centroids) {
                double distance = point.distanceTo(centroid);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestCentroid = centroid;
                }
            }
            closestCentroid.addPoint(point);
        }
    }

    private List<Point> initializeCentroids(int k, List<Point> points) {

        List<Point> centroids = new ArrayList<>();

        for (int i = 0; i < k; i++) {
            centroids.add(points.get(i));
        }

        return centroids;
    }
}
