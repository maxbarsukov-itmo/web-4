package ru.itmo.web.lab4.users.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDto {
  @NotBlank private String username;
  @NotBlank private String password;
}
