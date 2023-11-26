package ru.itmo.web.lab4.common.security.jwt;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtFilter extends GenericFilter {
  private final JwtUtils jwt;

  public JwtFilter(JwtUtils jwt) {
    this.jwt = jwt;
  }

  @Override
  public void doFilter(
    ServletRequest servletRequest,
    ServletResponse servletResponse,
    FilterChain filterChain
  ) throws IOException, ServletException {
    var token = jwt.resolveToken((HttpServletRequest) servletRequest);

    if (token != null && jwt.validateToken(token)) {
      Authentication auth = jwt.getAuthentication(token);
      if (auth != null) {
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    }

    filterChain.doFilter(servletRequest, servletResponse);
  }
}
