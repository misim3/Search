package com.example.search.repository;

import com.example.search.domain.Point;
import com.example.search.entity.BusStop;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusStopRepository extends JpaRepository<BusStop, Long> {

    Boolean existsByCityCodeAndNodeId(Integer cityCode, String nodeId);

    @Query("SELECT b FROM BusStop b WHERE b.longitude BETWEEN :x1 AND :x2 AND b.latitude BETWEEN :y1 AND :y2")
    List<BusStop> findAllByRange(double x1, double x2, double y1, double y2);

    BusStop findByCityCodeAndNodeId(Integer cityCode, String nodeId);
}
