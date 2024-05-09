package com.example.search.controller;

import com.example.search.controller.model.SearchRequest;
import com.example.search.service.KMeansClustering;
import com.example.search.service.SearchWay;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final SearchWay searchWay;
    private final KMeansClustering kMeansClustering;

    @PostMapping("/mid")
    public void getMidPoints(@RequestBody SearchRequest request) {

        // 중간 결과 받아서 변수에 담기
        searchWay.search(request.getSearchDetails());
        
        // 변수인 중간 지점들 중 일부 추출하는 메소드 호출
        kMeansClustering.cluster(3, new ArrayList<>(), 100);

        // 그리고 나서 탐색 결과 반환
    }
}
