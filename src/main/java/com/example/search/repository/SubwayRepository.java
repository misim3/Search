package com.example.search.repository;

import com.example.search.domain.Point;
import com.example.search.entity.Subway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubwayRepository extends JpaRepository<Subway, Long> {

    Boolean existsBySubwayId(String subwayId);

    @Query("SELECT s FROM Subway s WHERE s.longitude BETWEEN :x1 AND :x2 AND s.latitude BETWEEN :y1 AND :y2")
    List<Subway> findAllByRange(double x1, double x2, double y1, double y2);
}
