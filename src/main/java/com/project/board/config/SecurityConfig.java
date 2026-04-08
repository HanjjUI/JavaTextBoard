package com.project.board.config;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// セキュリティ設定（全開放）
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // CSRF無効
            .csrf(csrf -> csrf.disable())

            // 全て許可
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )

            // ログイン画面無効
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable());

        return http.build();
    }
}