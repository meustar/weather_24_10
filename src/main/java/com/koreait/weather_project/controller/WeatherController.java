package com.koreait.weather_project.controller;

import com.koreait.weather_project.service.WeatherService;
import com.koreait.weather_project.vo.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/weather")
    public String getWeather(@RequestParam(defaultValue = "Seoul") String city, Model model) {
        Weather weather = weatherService.getWeather(city);
        model.addAttribute("city", city);
        model.addAttribute("weather", weather);
        return "weather";
    }
}