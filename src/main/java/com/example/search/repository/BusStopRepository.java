package com.example.search.repository;

import com.example.search.entity.BusStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusStopRepository extends JpaRepository<BusStop, Long> {

    Boolean existsByCityCodeAndNodeId(Integer cityCode, String nodeId);
}
