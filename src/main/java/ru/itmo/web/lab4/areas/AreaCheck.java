package ru.itmo.web.lab4.areas;

import ru.itmo.web.lab4.attempts.Attempt;

import java.io.Serializable;

public interface AreaCheck extends Serializable {
  void checkHit(Attempt attempt);
}
