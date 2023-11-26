package ru.itmo.web.lab4.common.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Component
public class CryptoUtils {
  private static final int BCRYPT_STRENGTH = 11;

  public String digestPassword(String plainTextPassword) {
    try {
      return encoder().encode(plainTextPassword);
    } catch (Exception e) {
      throw new RuntimeException("Exception encoding password", e);
    }
  }

  public String digestPasswordSha(String plainTextPassword) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.update(plainTextPassword.getBytes(StandardCharsets.UTF_8));
      byte[] passwordDigest = md.digest();
      return new String(Base64.getEncoder().encode(passwordDigest));
    } catch (Exception e) {
      throw new RuntimeException("Exception encoding password", e);
    }
  }

  @Bean
  private PasswordEncoder encoder() {
    return new BCryptPasswordEncoder(BCRYPT_STRENGTH);
  }
}
