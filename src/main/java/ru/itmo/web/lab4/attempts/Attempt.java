package ru.itmo.web.lab4.attempts;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.data.annotation.CreatedDate;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

import ru.itmo.web.lab4.areas.AreaCheckService;
import ru.itmo.web.lab4.common.utils.validators.Contains;
import ru.itmo.web.lab4.users.User;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name="attempts")
public class Attempt implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="id", nullable=false, unique=true)
  private long id;

  // Radio {'-4','-3','-2','-1','0','1','2','3','4'} для координаты по оси X
  @NotNull
  @Column(name="x", nullable=false)
  private double x;

  // Text [-5 ... 5] для координаты по оси Y
  @NotNull
  @Column(name="y", nullable=false)
  private double y;

  // Radio {'1','2','3','4'} для задания радиуса области
  @NotNull @Contains(array={1, 2, 3, 4})
  @Column(name="r", nullable=false)
  private double r;

  @Column(name="result", nullable=false)
  private boolean result;

  @CreatedDate
  @Column(name="created_at", nullable=false)
  private Date createdAt;

  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="user_id")
  @ToString.Exclude
  private User creator;

  public Attempt(double x, double y, double r) {
    this.x = x;
    this.y = y;
    this.r = r;
    this.createdAt = new Date();
    checkResult();
  }

  private void checkResult(){
    new AreaCheckService().checkHit(this);
  }
}
