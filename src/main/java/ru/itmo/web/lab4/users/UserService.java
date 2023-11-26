package ru.itmo.web.lab4.users;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import ru.itmo.web.lab4.common.utils.CryptoUtils;

@Service
public class UserService {
  private final UserRepository repository;
  private final CryptoUtils crypto;

  public UserService(UserRepository repository, CryptoUtils crypto) {
    this.repository = repository;
    this.crypto = crypto;
  }

  @Transactional
  public User findByEmail(String email){
    return repository.getByEmail(email);
  }

  @Transactional
  public User findByEmailAndPassword(String email, String password){
    return repository.getByEmailAndPassword(email, crypto.digestPasswordSha(password));
  }

  @Transactional
  public boolean register(String email, String password){
    try {
      var user = new User(email, crypto.digestPasswordSha(password));
      repository.save(user);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
