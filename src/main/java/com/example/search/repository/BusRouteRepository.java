package com.example.search.repository;

import com.example.search.entity.BusRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusRouteRepository extends JpaRepository<BusRoute, Long> {

    Boolean existsByRouteIdAndCityCode(String routeId, Integer cityCode);

    Boolean existsByRouteIdAndNodeIdAndCityCode(String routeId, String nodeId, Integer cityCode);

    List<BusRoute> findAllByCityCodeAndNodeIdIsNullAndNodeOrdIsNull(Integer cityCode);

    List<BusRoute> findAllByCityCodeGreaterThanEqualAndNodeIdIsNullAndNodeOrdIsNull(Integer cityCode);
}
