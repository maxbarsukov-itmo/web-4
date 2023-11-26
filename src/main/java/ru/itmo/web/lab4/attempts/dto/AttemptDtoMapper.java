package ru.itmo.web.lab4.attempts.dto;

import jakarta.validation.constraints.*;
import org.springframework.stereotype.Component;
import ru.itmo.web.lab4.attempts.Attempt;
import ru.itmo.web.lab4.common.utils.validators.Contains;

@Component
public class AttemptDtoMapper {
  public Attempt toEntity(AttemptDto attemptDto) {
    @NotNull @Contains(array={-4, -3, -2, -1, 0, 1, 2, 3, 4}) double x = attemptDto.getX();
    @NotNull @Min(value = -5) @Max(value = 5) double y = attemptDto.getY();
    @NotNull @Contains(array={1, 2, 3, 4}) double r = attemptDto.getR();
    return new Attempt(x, y, r);
  }
}
