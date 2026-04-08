package com.project.board.config;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// セキュリティ設定（簡単バージョン）
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // CSRF無効（簡易開発用）
            .csrf(csrf -> csrf.disable())

            // URLごとのアクセス制御
            .authorizeHttpRequests(auth -> auth

                // 誰でもアクセス可能
                .requestMatchers("/", "/login", "/signup", "/user/**", "/board/list").permitAll()

                // それ以外はログイン必要
                .anyRequest().authenticated()
            )

            // デフォルトログイン画面を無効化
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable());

        return http.build();
    }
}