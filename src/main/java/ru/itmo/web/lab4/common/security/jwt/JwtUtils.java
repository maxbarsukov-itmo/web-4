package ru.itmo.web.lab4.common.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import javax.crypto.SecretKey;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

import ru.itmo.web.lab4.users.UserServiceDetails;

@Component
public class JwtUtils {
  private static final long TOKEN_VALIDITY = 3_600_000; // 1 hour

  @Value("${app.jwt.secret}")
  private String secret;
  private final UserServiceDetails userDetails;

  @Autowired
  public JwtUtils(UserServiceDetails userDetails) {
    this.userDetails = userDetails;
  }

  public String generateToken(String username, List<String> roles) {
    Claims claims = Jwts.claims().subject(username).add("roles", roles).build();
    Date now = new Date();
    return Jwts
      .builder()
      .claims(claims)
      .issuedAt(now)
      .expiration(new Date(now.getTime() + TOKEN_VALIDITY))
      .signWith(getKey()).compact();
  }

  public boolean validateToken(String token) {
    try {
      final Jws<Claims> claims = Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token);
      return !claims.getPayload().getExpiration().before(new Date());
    } catch (JwtException e) {
      return false;
    }
  }

  private boolean isTokenExpired(String token) {
    return getExpirationDate(token).before(new Date());
  }

  public String resolveToken(HttpServletRequest request) {
    var bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  public Authentication getAuthentication(String token) {
    var ud = this.userDetails.loadUserByUsername(getUsername(token));
    return new UsernamePasswordAuthenticationToken(ud, "", ud.getAuthorities());
  }

  public String getUsername(String token) {
    return getClaim(token, Claims::getSubject);
  }

  public Date getExpirationDate(String token) {
    return getClaim(token, Claims::getExpiration);
  }

  public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
    return claimsResolver.apply(claims);
  }

  private SecretKey getKey() {
    return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secret));
  }
}
