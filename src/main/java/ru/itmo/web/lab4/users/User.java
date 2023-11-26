package ru.itmo.web.lab4.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.List;

import ru.itmo.web.lab4.attempts.Attempt;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name="users")
public class User implements Serializable {
  public User(String username, String password){
    this.username = username;
    this.password = password;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="id", nullable=false, unique=true) @NotNull
  private long id;

  @Column(name="username", nullable=false) @NotNull
  private String username;

  @JsonIgnore
  @Column(name="password", nullable=false) @NotNull
  private String password;

  @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
  @JoinColumn(name="user_id")
  @JsonIgnore
  @ToString.Exclude
  private List<Attempt> attempts;
}
