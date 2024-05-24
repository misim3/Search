package com.example.search.repository;

import com.example.search.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    Boolean existsByStartNodeIdAndEndNodeId(String startNodeId, String endNodeId);

    @Query("SELECT c FROM Car c WHERE c.longitude BETWEEN :x1 AND :x2 AND c.latitude BETWEEN :y1 AND :y2")
    List<Car> findAllByRange(@Param("x1") double x1,@Param("x2") double x2,@Param("y1") double y1,@Param("y2") double y2);

    Boolean existsByStartNodeId(String startNodeId);

    List<Car> findAllByStartNodeId(String startNodeId);
}
