CREATE TABLE Weather (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         city VARCHAR(50) NOT NULL,
                         temperature DOUBLE,
                         humidity INT,
                         wind_speed DOUBLE,
                         timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);