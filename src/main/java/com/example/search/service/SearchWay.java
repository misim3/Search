package com.example.search.service;

import com.example.search.controller.model.SearchDetail;
import com.example.search.domain.Point;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SearchWay {

    private final SearchCar searchCar;

    private final SearchBus searchBus;

    private final SearchSubway searchSubway;

    private final SearchWalking searchWalking;

    public List<Point> search(List<SearchDetail> searchDetails) {
        // 여기서 시간제한, 약 이동수단별 도착가능한 노드 탐색 결과 반환, 교집합 탐색 중간지점 리턴
        // switch문 대신에 전략패턴 사용해서 호출. 여기서 DB에서 데이터 읽어와서 그래프 만드는 함수 호출해서 이동수단별 노드 탐색 메소드 호출시
        // 파라미터로 전달.

        // car, bus, subway graph 초기화
        // 자동차 그래프, 대중교통 그래프(버스와 지하철 그래프) 생성
        
        
        Set[] endNodes = new Set[searchDetails.size()];

        for (int i = 0; i < searchDetails.size(); i++) {
            endNodes[i] = new HashSet<>();
        }

        double timeLimit = 0;

        List<Point> intersections = new ArrayList<>();

        while (intersections.isEmpty()) {

            timeLimit += 60 * 10;

            for (int i = 0; i < searchDetails.size(); i++) {

                Set<Point> reachableNodes = null;

                // 자동차와 대중교통으로 분리
                switch (searchDetails.get(i).getVehicle()) {
                    case car:
                        reachableNodes = searchCar.search(searchDetails.get(i));
                        break;
                    case bus:
                        reachableNodes = searchBus.search(searchDetails.get(i));
                        break;
                    case subway:
                        reachableNodes = searchSubway.search(searchDetails.get(i));
                        break;
                    default:
                        break;
                }

                endNodes[i].addAll(reachableNodes);

            }

            intersections = findIntersection(endNodes);

        }

        return intersections;
    }

    private List<Point> findIntersection(Set[] endNodes) {
        // 다대다

        List<Point> res = new ArrayList<>();

        for (Set<Point> list : endNodes) {
            if (res.isEmpty()) {
                res = list;
            } else {
                res = searchWalking.searchAToB(res, list);
            }
        }

        return res;
    }
}
