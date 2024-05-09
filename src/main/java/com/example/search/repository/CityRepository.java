package com.example.search.repository;

import com.example.search.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    Boolean existsByCityCode(Integer cityCode);
}
