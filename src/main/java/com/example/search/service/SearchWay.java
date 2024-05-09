package com.example.search.service;

import com.example.search.controller.model.SearchDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchWay {

    private final SearchCar searchCar;

    private final SearchBus searchBus;

    private final SearchSubway searchSubway;

    public void search(List<SearchDetail> searchDetails) {
        // 여기서 시간제한, 약 이동수단별 도착가능한 노드 탐색 결과 반환, 교집합 탐색 중간지점 리턴

        // switch문 대신에 전략패턴 사용해서 호출. 여기서 DB에서 데이터 읽어와서 그래프 만드는 함수 호출해서 이동수단별 노드 탐색 메소드 호출시

        // 파라미터로 전달.
    }
}
