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
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import ru.itmo.web.lab4.common.security.jwt.JwtUtils;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {
  @Resource
  private JwtUtils jwt;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspection) throws Exception {
    final var mvc = new MvcRequestMatcher.Builder(introspection);

    http
      .httpBasic(HttpBasicConfigurer::disable)
      .csrf(CsrfConfigurer::disable)
      .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(authorizeRequests -> authorizeRequests
        .requestMatchers(antMatcher("/ws")).permitAll()
        .requestMatchers(antMatcher("/ws/*")).permitAll()
        .requestMatchers(mvc.pattern("/api/users")).permitAll()
        .requestMatchers(mvc.pattern("/api/users/*")).permitAll()
        .requestMatchers(mvc.pattern("/api/attempts")).permitAll()
        .requestMatchers(mvc.pattern("/api/attempts/*")).permitAll()
        .anyRequest().authenticated()
      )
      .apply(new JwtConfig(jwt));
    return http.build();
  }
}
