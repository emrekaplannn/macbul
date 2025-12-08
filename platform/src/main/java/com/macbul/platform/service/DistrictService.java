package com.macbul.platform.service;

import com.macbul.platform.dto.CityResponse;
import com.macbul.platform.dto.DistrictResponse;
import com.macbul.platform.util.City;
import com.macbul.platform.model.District;
import com.macbul.platform.repository.DistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DistrictService {

    private final DistrictRepository districtRepository;

    public List<DistrictResponse> getAllDistricts() {
        return districtRepository.findByActiveTrueOrderByCityAscDistrictNameAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<DistrictResponse> getDistrictsByCity(City city) {
        return districtRepository.findByCityAndActiveTrueOrderByDistrictNameAsc(city)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<CityResponse> getCities() {
        return districtRepository.findByDistrictNameIsNullAndActiveTrue()
                .stream()
                .map(d -> {
                    CityResponse res = new CityResponse();
                    res.setCity(d.getCity().name());
                    res.setDistrictId(d.getId());
                    return res;
                })
                .toList();
    }

    private DistrictResponse toResponse(District d) {
        DistrictResponse dto = new DistrictResponse();
        dto.setId(d.getId());
        dto.setCity(d.getCity().name());
        dto.setDistrictName(d.getDistrictName());
        return dto;
    }
}
