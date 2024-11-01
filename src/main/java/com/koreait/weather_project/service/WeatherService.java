package com.koreait.weather_project.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koreait.weather_project.repository.WeatherRepository;
import com.koreait.weather_project.vo.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;

@Service
public class WeatherService {

    @Autowired
    private WeatherRepository weatherRepository;

    public Weather getWeather(String cityName) {
        Weather weather = fetchWeatherFromAPI(cityName);
        if (weather != null) {
            weatherRepository.save(weather);
        }
        return weather;
    }

    private Weather fetchWeatherFromAPI(String cityName) {
        try {
            String apiKey = "YOUR_API_KEY";
            String apiUrl = "https://api.data.go.kr/weather/forecast?q=" + cityName + "&appid=" + apiKey + "&units=metric";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.toString());

            double temp = rootNode.path("main").path("temp").asDouble();
            int humidity = rootNode.path("main").path("humidity").asInt();
            double windSpeed = rootNode.path("wind").path("speed").asDouble();

            return new Weather(cityName, temp, humidity, windSpeed, LocalDateTime.now());

        } catch (Exception e) {
            System.out.println("Error fetching weather data: " + e.getMessage());
            return null;
        }
    }
}