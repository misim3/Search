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
        int flag = 0;
        for (Point medoid : medoids) {
            clusters.add(new Cluster(medoid));
        }
        // 최대 반복 횟수 동안 클러스터 업데이트
        for (int iter = 0; iter < maxIterations; iter++) {
            System.out.println("클러스터 업데이트 횟수: " + iter);
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
            flag = 0;
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
                if (newMedoid == cluster.getMedoid()) {
                    flag++;
                }
                cluster.setMedoid(newMedoid);
                cluster.clearPoints();
            }
            if (flag == 3) {
                break;
            }
        }
        return clusters.stream()
                .map(Cluster::getMedoid)
                .toList();
    }

    private List<Point> initializeMedoids(int k, List<Point> data) {

        List<Point> medoids = new ArrayList<>();

        Random random = new Random();

        // 첫 번째 메도이드를 무작위로 선택
        medoids.add(data.get(random.nextInt(data.size())));

        // 나머지 메도이드를 K-medoids++ 방식으로 선택
        for (int i = 1; i < k; i++) {
            double[] distances = new double[data.size()];
            double totalDistance = 0;

            for (int j = 0; j < data.size(); j++) {
                double minDistance = Double.MAX_VALUE;
                for (Point medoid : medoids) {
                    double distance = data.get(j).distanceTo(medoid);
                    if (distance < minDistance) {
                        minDistance = distance;
                    }
                }
                distances[j] = minDistance;
                totalDistance += minDistance;
            }

            double threshold = random.nextDouble() * totalDistance;
            double cumulativeDistance = 0;
            for (int j = 0; j < data.size(); j++) {
                cumulativeDistance += distances[j];
                if (cumulativeDistance >= threshold) {
                    medoids.add(data.get(j));
                    break;
                }
            }
        }
        return medoids;
    }
}
