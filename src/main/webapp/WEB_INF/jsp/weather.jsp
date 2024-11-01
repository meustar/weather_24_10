<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
    <head>
        <meta charset="UTF-8">
        <title>Weather Information</title>
    </head>
    <body>
        <h2>Weather Information</h2>
        <form action="/weather" method="get">
            <label for="city">Enter city name:</label>
            <input type="text" id="city" name="city" required>
            <button type="submit">Get Weather</button>
        </form>

        <div th:if="${weather}">
            <h3>Weather in <span th:text="${city}"></span></h3>
            <p>Temperature: <span th:text="${weather.temperature}"></span>°C</p>
            <p>Humidity: <span th:text="${weather.humidity}"></span>%</p>
            <p>Wind Speed: <span th:text="${weather.windSpeed}"></span> m/s</p>
        </div>
    </body>
</html>
