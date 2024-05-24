package com.example.search.controller.model;

import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Data
@Builder
@Schema(name = "탐색 응답 DTO")
public class SearchResponse {

    @Schema(name = "addresses", description = "3곳의 중간 지점 추천 주소 정보", example = "인천 연수구 송도동 48", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> addresses;
}
