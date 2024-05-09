package com.example.search.repository;

import com.example.search.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    Boolean existsByStartNodeIdAndEndNodeId(String startNodeId, String endNodeId);
}
