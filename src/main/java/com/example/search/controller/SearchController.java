package com.example.search.controller;

import com.example.search.controller.model.SearchRequest;
import com.example.search.controller.model.SearchResponse;
import com.example.search.domain.Point;
import com.example.search.domain.Vehicle;
import com.example.search.service.KMedoids;
import com.example.search.service.SearchWay;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final SearchWay searchWay;
    private final KMedoids kMedoids;

    @Operation(summary = "중간 지점 추천", description = "중간 지점 추천하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "중간 지점 추천 성공.")})
    @PostMapping("/mid")
    public SearchResponse getMidPoints(@RequestBody SearchRequest request) {

        // 중간 지점 탐색
        List<Vehicle> searchLists = searchWay.search(request.getSearchDetails());

//        System.out.println("중간 지점 갯수: " + searchLists.size());
//        for (Vehicle vehicle : searchLists) {
//            System.out.println("중간 지점 식별 정보: "+ vehicle.getId());
//        }
//
//        System.out.println("------------------------------");
        // 중간 지점 선별
        List<Point> res = kMedoids.cluster(3,
                searchLists.stream()
                .map(s -> new Point(s.getLongitude(), s.getLatitude()))
                .toList(),
                100);

//        for (Point point : res) {
//            System.out.println("선별된 중간 장소 위도 " + point.getY());
//            System.out.println("선별된 중간 장소 경도 " + point.getX());
//        }

        

        // 그리고 나서 탐색 결과 반환
        return SearchResponse.builder()
                .addresses(null)
                .build();
    }
}
