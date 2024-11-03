package com.koreait.weather_project.controller;

import org.springframework.ui.Model;
import com.koreait.weather_project.service.WeatherService;
import com.koreait.weather_project.vo.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/weather")
    public String getWeather(@RequestParam(defaultValue = "Seoul") String city, Model model) {
        // 예시로 서울의 nx, ny 값을 넣음
        String nx = "60";
        String ny = "127";

        Weather weather = weatherService.getWeather(city, nx, ny);
        model.addAttribute("city", city);
        model.addAttribute("weather", weather);
        return "weather";
    }
}