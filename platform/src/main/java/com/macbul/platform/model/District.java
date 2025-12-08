package com.macbul.platform.model;

import com.macbul.platform.util.City;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "districts")
@Data
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "city", nullable = false, length = 50)
    private City city;

    @Column(name = "district_name", length = 100)
    private String districtName;  // NULL → city-only kayıt

    @Column(name = "active")
    private Boolean active = true;
}
