package com.example.search.controller.model;

import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Data
@Builder
@Schema(name = "탐색 응답 DTO")
public class SearchResponse {

    @Schema(name = "addresses", description = "3곳의 중간 지점 추천 주소 정보 목록", example = "{ 대한민국 인천광역시 인천스타트업파크, 대한민국 인천광역시 송도더샵하버뷰2차, 대한민국 인천광역시 송도자이하버뷰 }", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> addresses;
}
