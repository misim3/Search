package com.example.search.repository;

import com.example.search.entity.SubwayRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubwayRouteRepository extends JpaRepository<SubwayRoute, Long> {
}
