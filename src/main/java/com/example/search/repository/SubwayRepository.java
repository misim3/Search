package com.example.search.repository;

import com.example.search.entity.Subway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubwayRepository extends JpaRepository<Subway, Long> {

    Boolean existsBySubwayId(String subwayId);
}
