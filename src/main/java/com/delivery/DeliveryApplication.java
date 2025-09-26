package com.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@SpringBootApplication
public class DeliveryApplication {

    public static void main(String[] args) {
        // 환경변수 로드
        loadEnvFile();
        SpringApplication.run(DeliveryApplication.class, args);
    }

    private static void loadEnvFile() {
        try {
            Path envPath = Paths.get(".env");
            if (Files.exists(envPath)) {
                List<String> lines = Files.readAllLines(envPath);
                for (String line : lines) {
                    if (line.trim().isEmpty() || line.startsWith("#")) {
                        continue;
                    }
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim();
                        if (System.getenv(key) == null) { // 이미 설정된 환경변수가 없을 때만 설정
                            System.setProperty(key, value);
                        }
                    }
                }
                System.out.println(".env 파일이 로드되었습니다.");
            } else {
                System.out.println(".env 파일을 찾을 수 없습니다.");
            }
        } catch (IOException e) {
            System.err.println(".env 파일 로드 중 오류 발생: " + e.getMessage());
        }
    }
}