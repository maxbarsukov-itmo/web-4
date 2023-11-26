package ru.itmo.web.lab4.users;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import ru.itmo.web.lab4.common.security.UserPrincipal;

@Service
public class UserServiceDetails implements UserDetailsService {
  private final UserService service;

  public UserServiceDetails(UserService service) {
    this.service = service;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    var user = service.findByEmail(email);
    if (user == null) throw new UsernameNotFoundException("User not found");
    else return new UserPrincipal(user);
  }
}
