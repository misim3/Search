package com.example.search.service;

import com.example.search.domain.Point;
import com.example.search.repository.BusStopRepository;
import com.example.search.repository.CarRepository;
import com.example.search.repository.SubwayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchWalking {

    private final CarRepository carRepository;
    private final BusStopRepository busStopRepository; ;
    private final SubwayRepository subwayRepository;

    public List<Point> searchAToB(List<Point> a, List<Point> b) {
        List<Point> result = new ArrayList<>();

        for (Point point : a) {
            result.addAll(searchOneToList(point, b));
        }

        return result;
    }

    // 출발지에서 이동수단별 인접한 노드 탐색
    // 조건은 도보 이동시간 10분 이내
    public List<Point> searchOneToDB(Point s, String type) {

        // 위도에 따른 1도당 거리 계산
        double distancePerDegreeLatitude = calculateDistancePerDegree(s.getY());
        // 경도에 따른 1도당 거리 계산 (위도와 무관)
        // 지구 반지름 대략 6371km
        double distancePerDegreeLongitude = 6371.0 * Math.PI / 180.0;

        // 도보로 10분에 이동 가능한 거리 (단위: km)
        double walkingIn10Minutes = 10.0 / 60.0 * 5.0; // 10분을 시간으로 환산하여 km로 변환 (보행 속도: 5 km/h)

        double x1 = s.getX() - (walkingIn10Minutes / distancePerDegreeLongitude);
        double x2 = s.getX() + (walkingIn10Minutes / distancePerDegreeLongitude);
        double y1 = s.getY() - (walkingIn10Minutes / distancePerDegreeLatitude);
        double y2 = s.getY() + (walkingIn10Minutes / distancePerDegreeLatitude);

        switch (type) {
            case "car":
                return carRepository.findAllByRange(x1, x2, y1, y2).stream()
                        .map(car -> Point.builder()
                                .x(car.getLongitude())
                                .y(car.getLatitude())
                                .build())
                        .toList();
            case "bus":
                return busStopRepository.findAllByRange(x1, x2, y1, y2).stream()
                        .map(busStop -> Point.builder()
                                .x(busStop.getLongitude())
                                .y(busStop.getLatitude())
                                .build())
                        .toList();
            case "subway":
                return subwayRepository.findAllByRange(x1, x2, y1, y2).stream()
                        .map(subway -> Point.builder()
                                .x(subway.getLongitude())
                                .y(subway.getLatitude())
                                .build())
                        .toList();
            default:
                return null;
        }
    }

    private double calculateDistancePerDegree(double y) {
        double latitudeRadians = Math.toRadians(y);
        double cosLatitude = Math.cos(latitudeRadians);

        // 위도에 따른 지구의 둘레 계산
        double circumference = 2 * Math.PI * 6371.0 * cosLatitude;

        // 1도당 거리 계산

        return circumference / 360.0;
    }

    public List<Point> searchOneToList(Point s, List<Point> data) {
        List<Point> result = new ArrayList<Point>();

        for (int i = 0; i < data.size(); i++) {
            Point p = data.get(i);

            if (walkingTime(s, p)) {
                result.add(p);
            }
        }

        return result;
    }

    private boolean walkingTime(Point s, Point p) {
        double distance = calculateDistance(s, p);
        int walkingTime = predictWalkingTime(distance);

        if (walkingTime <= 10) {
            return true;
        } else {
            return false;
        }
    }

    private int predictWalkingTime(double distance) {
        // 평균 보행 속도 5.0 km/h
        double walkingTimeInHours = distance / 5.0;

        return (int) (walkingTimeInHours * 60);
    }

    // Haversine 공식 사용
    private double calculateDistance(Point s, Point p) {
        final int R = 6371; // 지구 반지름 (단위: km)

        double lonDistance = Math.toRadians(p.getX() - s.getX());
        double latDistance = Math.toRadians(p.getY() - s.getY());

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(s.getX())) * Math.cos(Math.toRadians(p.getX()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
