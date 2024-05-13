package com.example.search.domain;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class SGraph {

    private Map<String, SNode> subways;

    public SGraph() {
        this.subways = new HashMap<>();
    }
}
