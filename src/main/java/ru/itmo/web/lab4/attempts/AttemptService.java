package ru.itmo.web.lab4.attempts;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

import ru.itmo.web.lab4.users.User;

@Service
public class AttemptService {
  private final AttemptRepository repository;

  public AttemptService(AttemptRepository repository) {
    this.repository = repository;
  }
  @Transactional
  public List<Attempt> getAllAttemptByCreatorId(long userId) {
    return repository.getAllByCreatorId(userId);
  }

  @Transactional
  public void addAttemptByCreator(Attempt attempt, User creator) {
    attempt.setCreator(creator);
    repository.save(attempt);
  }

  @Transactional
  public void deleteAllByCreatorId(long userId) {
    repository.deleteAllByCreatorId(userId);
  }
}
