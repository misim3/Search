package com.example.search.service;

import com.example.search.domain.Cluster;
import com.example.search.domain.Point;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class KMedoids {

    public List<Point> cluster(int k, List<Point> data, int maxIterations) {

        List<Point> medoids = initializeMedoids(k, data);

        List<Cluster> clusters = new ArrayList<>();
        for (Point medoid : medoids) {
            clusters.add(new Cluster(medoid));
        }

        // 최대 반복 횟수 동안 클러스터 업데이트
        for (int iter = 0; iter < maxIterations; iter++) {
            // 클러스터 할당
            for (Point point : data) {
                double minDistance = Double.MAX_VALUE;
                Cluster nearestCluster = null;
                for (Cluster cluster : clusters) {
                    double distance = point.distanceTo(cluster.getMedoid());
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestCluster = cluster;
                    }
                }
                nearestCluster.getPoints().add(point);
            }

            // 클러스터 업데이트
            for (Cluster cluster : clusters) {
                double minCost = Double.MAX_VALUE;
                Point newMedoid = cluster.getMedoid();
                for (Point point : cluster.getPoints()) {
                    double cost = 0;
                    for (Point otherPoint : cluster.getPoints()) {
                        cost += point.distanceTo(otherPoint);
                    }
                    if (cost < minCost) {
                        minCost = cost;
                        newMedoid = point;
                    }
                }
                cluster.setMedoid(newMedoid);
                cluster.clearPoints();
            }
        }

        return clusters.stream()
                .map(Cluster::getMedoid)
                .toList();
    }

    private List<Point> initializeMedoids(int k, List<Point> data) {

        List<Point> medoids = new ArrayList<>();

        Random random = new Random();
        for (int i = 0; i < k; i++) {
            int index = random.nextInt(data.size());
            medoids.add(data.get(index));
        }

        return medoids;
    }
}
