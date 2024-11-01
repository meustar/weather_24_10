package com.koreait.weather_project.repository;

import com.koreait.weather_project.vo.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
    List<Weather> findByCity(String city);
}