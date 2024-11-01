package com.koreait.weather_project.vo;

import jakarta.persistence.*;
import jakarta.persistence.GenerationType;

import java.time.LocalDateTime;

@Entity
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private double temperature;
    private int humidity;
    private double windSpeed;
    private LocalDateTime timestamp;

    public Weather() {}

    public Weather(String city, double temperature, int humidity, double windSpeed, LocalDateTime timestamp) {
        this.city = city;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.timestamp = timestamp;
    }

    // Getter methods
    public double getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }
}
