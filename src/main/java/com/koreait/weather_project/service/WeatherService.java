package com.koreait.weather_project.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koreait.weather_project.repository.WeatherRepository;
import com.koreait.weather_project.vo.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherService {

    @Autowired
    private WeatherRepository weatherRepository;

    // OpenWeather API URL
    private final String apiUrl = "https://api.openweathermap.org/data/2.5/weather";

    // OpenWeather API Key (발급받은 API Key 입력)
    private final String apiKey = "6092655f10eec7e6a4e71bbdcc27a3d3";

    // 한글 도시 이름을 영문으로 매핑
    private final Map<String, String> cityMapping;
    public WeatherService() {
        cityMapping = new HashMap<>();
        cityMapping.put("서울", "Seoul");
        cityMapping.put("대전", "Daejeon");
        cityMapping.put("대구", "Daegu");
        cityMapping.put("부산", "Busan");
        cityMapping.put("광주", "Gwangju");
        cityMapping.put("울산", "Ulsan");
        cityMapping.put("인천", "Incheon");
    }

    public Weather getWeather(String cityName, String nx, String ny) {
        Weather weather = fetchWeatherFromAPI(cityName);
        if (weather != null) {
            weatherRepository.save(weather);
        }
        return weather;
    }

    private Weather fetchWeatherFromAPI(String cityName) {
        try {
            // 한글 도시 이름을 영문으로 변환
            String city = cityMapping.getOrDefault(cityName, cityName);
            String encodedCityName = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());

            // URL 구성
            String url = apiUrl + "?q=" + encodedCityName  + "&appid=" + apiKey + "&units=metric";

            System.out.println("Request URL: " + url); // 디버깅을 위한 URL 출력

            // API 호출
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            // JSON 응답 파싱
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);

            // 날씨 데이터 추출
            double temperature = rootNode.path("main").path("temp").asDouble();
            int humidity = rootNode.path("main").path("humidity").asInt();
            double windSpeed = rootNode.path("wind").path("speed").asDouble();

            return new Weather(cityName, temperature, humidity, windSpeed, LocalDateTime.now());

        } catch (Exception e) {
            System.out.println("Error fetching weather data: " + e.getMessage());
            return null;
        }
    }
}
