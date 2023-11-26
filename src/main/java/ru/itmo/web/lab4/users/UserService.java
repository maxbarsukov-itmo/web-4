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
  public User findByUsername(String username){
    return repository.getByUsername(username);
  }

  @Transactional
  public User findByUsernameAndPassword(String username, String password){
    return repository.getByUsernameAndPassword(username, crypto.digestPasswordSha(password));
  }

  @Transactional
  public boolean register(String username, String password){
    try {
      var user = new User(username, crypto.digestPasswordSha(password));
      repository.save(user);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
