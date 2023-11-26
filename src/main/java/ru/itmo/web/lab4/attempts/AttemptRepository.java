package ru.itmo.web.lab4.attempts;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttemptRepository extends JpaRepository<Attempt, Long> {
  List<Attempt> getAllByCreatorId(long userId);

  @Modifying
  @Query("delete from Attempt a where a.userId = ?1")
  void deleteAllByCreatorId(long userId);
}
