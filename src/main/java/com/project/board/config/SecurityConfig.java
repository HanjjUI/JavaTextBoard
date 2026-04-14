package com.project.board.config;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// セキュリティに関する設定クラスです
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // CSRFを無効にしています
            // 今回は学習用プロジェクトなので、動作確認をしやすくするために設定しています
            .csrf(csrf -> csrf.disable())

            // すべてのリクエストを許可しています
            // 認可の細かい設定はまだ入れていません
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )

            // Spring Securityのデフォルトログイン画面を使わないようにしています
            .formLogin(form -> form.disable())

            // Basic認証も使わないようにしています
            .httpBasic(basic -> basic.disable());

        return http.build();
    }
}
