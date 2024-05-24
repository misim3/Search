package com.example.search.controller.model;

import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@Schema(name = "탐색 상세 정보 DTO")
public class SearchDetail {

    @Schema(name = "userName", description = "모임원 식별 정보", example = "철수", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userName;

    @Schema(name = "address", description = "모임원 출발 주소로 지번 주소로 입력해야 한다.", example = "인천 연수구 송도동 48", requiredMode = Schema.RequiredMode.REQUIRED)
    private String address;

    @Schema(name = "vehicle", description = "이동수단 정보로 car, bus, subway만 가능하다.", example = "car", requiredMode = Schema.RequiredMode.REQUIRED)
    private String vehicle;
}
