package ru.itmo.web.lab4.attempts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import ru.itmo.web.lab4.users.User;

@Repository
public interface AttemptRepository extends JpaRepository<Attempt, Long> {
  List<Attempt> getAllByCreatorId(long userId);

  void deleteByCreator(User creator);
}
