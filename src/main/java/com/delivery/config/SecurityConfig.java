package com.delivery.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable());

        // 임시 모든 요청 허용, 추후 JWT 적용 시 변경
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests.
                        anyRequest().permitAll()
        );

        http.formLogin((formLogin) -> formLogin.disable());

        return http.build();
    }
}
