package com.example.search.controller.model;

import lombok.Data;

import java.util.List;

@Data
public class SearchRequest {

    private List<SearchDetail> searchDetails;
}