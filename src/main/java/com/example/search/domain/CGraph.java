package com.example.search.domain;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CGraph {

    private Map<String, CNode> nodes;

    public CGraph() {
        this.nodes = new HashMap<>();
    }
}
