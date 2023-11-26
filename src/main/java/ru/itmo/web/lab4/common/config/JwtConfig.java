package ru.itmo.web.lab4.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.stereotype.Component;
import ru.itmo.web.lab4.common.security.jwt.JwtFilter;
import ru.itmo.web.lab4.common.security.jwt.JwtUtils;

@Component
public class JwtConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
  private final JwtUtils jwt;

  @Autowired
  public JwtConfig(JwtUtils jwt) {
    this.jwt = jwt;
  }

  @Override
  public void configure(HttpSecurity builder) {
    builder.addFilterBefore(new JwtFilter(jwt), UsernamePasswordAuthenticationFilter.class);
  }
}
