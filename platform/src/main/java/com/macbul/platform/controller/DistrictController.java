package com.macbul.platform.controller;

import com.macbul.platform.dto.CityResponse;
import com.macbul.platform.dto.DistrictResponse;
import com.macbul.platform.util.City;
import com.macbul.platform.service.DistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class DistrictController {

    private final DistrictService districtService;

    @GetMapping("/districts")
    public ResponseEntity<List<DistrictResponse>> getAllDistricts() {
        return ResponseEntity.ok(districtService.getAllDistricts());
    }

    @GetMapping("/districts/city/{city}")
    public ResponseEntity<List<DistrictResponse>> getDistrictsByCity(@PathVariable("city") City city) {
        return ResponseEntity.ok(districtService.getDistrictsByCity(city));
    }

    @GetMapping("/cities")
    public ResponseEntity<List<CityResponse>> getCities() {
        return ResponseEntity.ok(districtService.getCities());
    }
}
