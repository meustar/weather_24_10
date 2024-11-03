package com.koreait.weather_project.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koreait.weather_project.repository.WeatherRepository;
import com.koreait.weather_project.vo.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class WeatherService {

    @Autowired
    private WeatherRepository weatherRepository;

    private final String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst";

    public Weather getWeather(String cityName, String nx, String ny) {
        Weather weather = fetchWeatherFromAPI(cityName, nx, ny);
        if (weather != null) {
            weatherRepository.save(weather);
        }
        return weather;
    }

    private Weather fetchWeatherFromAPI(String cityName, String nx, String ny) {
        try {
            // 발표 일자: 현재 날짜로 설정
            String baseDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            // 발표 시각: 06시 (정시 단위, 예시로 설정)
            String baseTime = "0600";
            // 서비스 키: 공공데이터 포털에서 발급받은 인증키 사용
            String apiKey = "API KEY"; // 새로 발급받은 API 키 입력

            // URL 구성
            String url = apiUrl + "?serviceKey=" + apiKey
                    + "&numOfRows=10&pageNo=1&dataType=JSON"
                    + "&base_date=" + baseDate
                    + "&base_time=" + baseTime
                    + "&nx=" + nx
                    + "&ny=" + ny;

            System.out.println("Request URL: " + url); // 디버깅을 위한 URL 출력

            // API 호출
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            // XML 응답 처리
            if (response.startsWith("<")) {
                System.out.println("Received XML response: " + response);
                return null;
            }

            // JSON 응답 파싱
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            JsonNode items = rootNode.path("response").path("body").path("items").path("item");

            // 날씨 데이터 추출
            double temperature = 0.0;
            int humidity = 0;
            double windSpeed = 0.0;

            for (JsonNode item : items) {
                String category = item.path("category").asText();
                if ("T1H".equals(category)) { // 기온
                    temperature = item.path("obsrValue").asDouble();
                } else if ("REH".equals(category)) { // 습도
                    humidity = item.path("obsrValue").asInt();
                } else if ("WSD".equals(category)) { // 풍속
                    windSpeed = item.path("obsrValue").asDouble();
                }
            }

            return new Weather(cityName, temperature, humidity, windSpeed, LocalDate.now().atStartOfDay());

        } catch (Exception e) {
            System.out.println("Error fetching weather data: " + e.getMessage());
            return null;
        }
    }
}
