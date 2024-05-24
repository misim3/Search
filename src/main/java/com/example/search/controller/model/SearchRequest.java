package com.example.search.controller.model;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Data
@Schema(name="탐색 요청 DTO")
public class SearchRequest {

    @Schema(name = "searchDetails", description = "탐색 상세 정보 목록", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<SearchDetail> searchDetails;
}