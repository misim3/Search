package com.example.search.controller;

import com.example.search.controller.model.SearchRequest;
import com.example.search.domain.Point;
import com.example.search.domain.Vehicle;
import com.example.search.service.KMedoids;
import com.example.search.service.SearchWay;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final SearchWay searchWay;
    private final KMedoids kMedoids;

    @PostMapping("/mid")
    public void getMidPoints(@RequestBody SearchRequest request) {

        // 중간 결과 받아서 변수에 담기
        List<Vehicle> searchLists = searchWay.search(request.getSearchDetails());
        
        // 변수인 중간 지점들 중 일부 추출하는 메소드 호출
        List<Point> res = kMedoids.cluster(2,
                searchLists.stream()
                .map(s -> new Point(s.getLongitude(), s.getLatitude()))
                .toList(),
                100);


        // 그리고 나서 탐색 결과 반환
    }
}
