package ru.itmo.web.lab4.common.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import ru.itmo.web.lab4.common.security.jwt.JwtUtils;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {
  @Resource
  private JwtUtils jwt;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .httpBasic(HttpBasicConfigurer::disable)
      .csrf(CsrfConfigurer::disable)
      .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(authorizeRequests -> authorizeRequests
        .requestMatchers("/api/users").permitAll()
        .requestMatchers("/api/users/*").permitAll()
        .requestMatchers("/api/attempts").permitAll()
        .requestMatchers("/api/attempts/*").permitAll()
        .anyRequest().authenticated()
      )
      .apply(new JwtConfig(jwt));
    return http.build();
  }
}
