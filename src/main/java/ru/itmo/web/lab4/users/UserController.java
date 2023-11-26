package ru.itmo.web.lab4.users;

import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.ArrayList;

import ru.itmo.web.lab4.users.dto.UserDto;
import ru.itmo.web.lab4.common.utils.CryptoUtils;
import ru.itmo.web.lab4.common.security.jwt.JwtUtils;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api/users")
public class UserController {
  private final AuthenticationManager authManager = authentication -> null;
  private final CryptoUtils crypto;
  private final JwtUtils jwt;
  private final UserService service;

  public UserController(JwtUtils jwt, CryptoUtils crypto, UserService service) {
    this.jwt = jwt;
    this.crypto = crypto;
    this.service = service;
  }

  @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
  private ResponseEntity<String> login(@Valid @RequestBody UserDto userDto){
    var username = userDto.getUsername();
    var password = crypto.digestPasswordSha(userDto.getPassword());
    var user = service.findByUsernameAndPassword(username, password);

    if (user != null) {
      authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
      String token = jwt.generateToken(username, new ArrayList<>() {{ add("USER"); }});
      return ResponseEntity.ok(token);
    }

    return new ResponseEntity<>("No such username and password", HttpStatus.NOT_FOUND);
  }

  @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
  private ResponseEntity<String> register(@Valid @RequestBody UserDto userDto){
    var username = userDto.getUsername();
    var password = crypto.digestPasswordSha(userDto.getPassword());
    var user = service.findByUsername(username);

    if (user == null) {
      if (service.register(username, password)) {
        return new ResponseEntity<>("User has been created", HttpStatus.CREATED);
      }
      return new ResponseEntity<>("Cannot register user", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    return new ResponseEntity<>("User exists with the same username", HttpStatus.BAD_REQUEST);
  }
}
