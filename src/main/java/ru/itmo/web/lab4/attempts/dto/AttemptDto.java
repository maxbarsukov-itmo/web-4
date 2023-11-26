package ru.itmo.web.lab4.attempts.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.itmo.web.lab4.common.utils.validators.Contains;

import java.util.stream.DoubleStream;

@Data
public class AttemptDto {
  @NotBlank @Contains(array={-4, -3, -2, -1, 0, 1, 2, 3, 4}) private double x;
  @NotBlank @Min(value = -5) @Max(value = 5) private double y;
  @NotBlank @Contains(array={1, 2, 3, 4}) private double r;

  public boolean validate() {
    if (DoubleStream.of(-4, -3, -2, -1, 0, 1, 2, 3, 4).noneMatch(v -> v == x)) {
      return false;
    }
    if (DoubleStream.of(1, 2, 3, 4).noneMatch(v -> v == r)) {
      return false;
    }
    return (y >= -5 && y <= 5);
  }
}
