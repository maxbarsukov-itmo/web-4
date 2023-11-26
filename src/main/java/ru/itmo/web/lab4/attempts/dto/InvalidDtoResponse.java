package ru.itmo.web.lab4.attempts.dto;

public class InvalidDtoResponse {
  public String code = "UNPROCESSABLE_ENTITY";
  public String message = "Invalid attempt coordinates.";
}
