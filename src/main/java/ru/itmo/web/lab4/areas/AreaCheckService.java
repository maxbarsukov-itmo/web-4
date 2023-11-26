package ru.itmo.web.lab4.areas;

import org.springframework.stereotype.Component;
import ru.itmo.web.lab4.attempts.Attempt;

import java.io.Serializable;

@Component
@AreaCheckQualifier
public class AreaCheckService implements AreaCheck, Serializable {
  @Override
  public void checkHit(Attempt attempt) {
    attempt.setResult(attemptIsInArea(attempt));
  }

  private boolean attemptIsInArea(Attempt attempt) {
    var x = attempt.getX();
    var y = attempt.getY();
    var r = attempt.getR();
    return attemptIsInRect(x, y, r) || attemptIsInTriangle(x, y, r) || attemptIsInSector(x, y, r);
  }

  private boolean attemptIsInRect(double x, double y, double r) {
    return (x >= 0 && y <= 0) && (x <= r && y >= -r);
  }

  private boolean attemptIsInSector(double x, double y, double r) {
    return (x >= 0 && y >= 0) && (x*x + y*y <= r*r / 4);
  }

  private boolean attemptIsInTriangle(double x, double y, double r) {
    return (x <= 0 && y >= 0)  && (x-y >= -r);
  }
}
