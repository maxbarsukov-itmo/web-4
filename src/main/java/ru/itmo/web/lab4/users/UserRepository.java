package ru.itmo.web.lab4.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User getByEmail(String email);
  User getByEmailAndPassword(String email, String password);
}
