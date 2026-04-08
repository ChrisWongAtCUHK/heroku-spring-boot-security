package com.heroku.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(auth -> auth
        .requestMatchers(HttpMethod.POST, "/users").permitAll()
        .requestMatchers(HttpMethod.GET, "/users/?*").hasAnyAuthority("ADMIN", "NORMAL")
        .requestMatchers(HttpMethod.GET, "/users").hasAuthority("ADMIN")
        .anyRequest().authenticated())
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(withDefaults()); // 開啟內建登入畫面 (測試用途)

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}