package ru.itmo.web.lab4.attempts.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.itmo.web.lab4.common.utils.validators.Contains;

import java.util.stream.DoubleStream;

@Data
public class AttemptDto {
  private double x;
  private double y;
  @Contains(array={1, 2, 3, 4}) private double r;

  public boolean validate() {
    return DoubleStream.of(1, 2, 3, 4).anyMatch(v -> v == r);
  }
}
