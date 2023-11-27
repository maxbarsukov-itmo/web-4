package ru.itmo.web.lab4.attempts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.web.lab4.common.utils.validators.Contains;

import java.util.stream.DoubleStream;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttemptDto {
  private double x;
  private double y;
  @Contains(array={1, 2, 3, 4}) private double r;

  public boolean validate() {
    return DoubleStream.of(1, 2, 3, 4).anyMatch(v -> v == r);
  }
}
