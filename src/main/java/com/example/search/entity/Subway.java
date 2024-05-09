package com.example.search.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Subway {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subwayId;

    private String name;

    private String routeName;

    @Builder
    public Subway(String subwayId, String name, String routeName) {
        this.subwayId = subwayId;
        this.name = name;
        this.routeName = routeName;
    }
}
