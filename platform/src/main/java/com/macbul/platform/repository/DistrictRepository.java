package com.macbul.platform.repository;

import com.macbul.platform.util.City;
import com.macbul.platform.model.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DistrictRepository extends JpaRepository<District, Integer> {

    List<District> findByActiveTrueOrderByCityAscDistrictNameAsc();

    List<District> findByCityAndActiveTrueOrderByDistrictNameAsc(City city); 

    List<District> findByDistrictNameIsNullAndActiveTrue(); // only city-only rows

    Optional<District> findById(Integer id);

    Optional<Integer> findIdByCityAndDistrictName(City city, String districtName);

    Optional<Integer> findIdByCityAndDistrictNameIsNull(City city);

}
