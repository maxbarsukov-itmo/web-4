package ru.itmo.web.lab4.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.ArrayList;

import ru.itmo.web.lab4.LoginRequest;
import ru.itmo.web.lab4.LoginResponse;
import ru.itmo.web.lab4.RegisterRequest;
import ru.itmo.web.lab4.RegisterResponse;
import ru.itmo.web.lab4.common.security.jwt.JwtUtils;
import ru.itmo.web.lab4.common.utils.CryptoUtils;
import ru.itmo.web.lab4.notifications.NotificationService;
import static ru.itmo.web.lab4.common.config.WebServiceConfig.NAMESPACE_URI;

@Endpoint
public class UserEndpoint {
  private final AuthenticationManager authManager = authentication -> null;
  private final CryptoUtils crypto;
  private final JwtUtils jwt;
  private final UserService service;
  private final NotificationService notificationService;

  @Autowired
  public UserEndpoint(JwtUtils jwt, CryptoUtils crypto, UserService service, NotificationService notificationService) {
    this.jwt = jwt;
    this.crypto = crypto;
    this.service = service;
    this.notificationService = notificationService;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "loginRequest")
  @ResponsePayload
  public LoginResponse login(@RequestPayload LoginRequest request) {
    var response = new LoginResponse();

    var email = request.getUserDto().getEmail();
    var password = crypto.digestPasswordSha(request.getUserDto().getPassword());
    var user = service.findByEmailAndPassword(email, password);

    if (user != null) {
      authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
      String token = jwt.generateToken(email, new ArrayList<>() {{ add("USER"); }});

      response.setCode(HttpStatus.OK.value());
      response.setStatus("Вы успешно вошли в аккаунт.");
      response.setToken(token);
      return response;
    }

    response.setCode(HttpStatus.NOT_FOUND.value());
    response.setStatus("Нет пользователя с таким email и паролем!");
    return response;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "registerRequest")
  @ResponsePayload
  public RegisterResponse register(@RequestPayload RegisterRequest request) {
    var response = new RegisterResponse();

    var email = request.getUserDto().getEmail();
    var password = crypto.digestPasswordSha(request.getUserDto().getPassword());
    var user = service.findByEmail(email);

    if (user == null) {
      if (service.register(email, password)) {
        notificationService.greeting(email);

        response.setCode(HttpStatus.CREATED.value());
        response.setStatus("Пользователь был создан.");
        return response;
      }

      response.setCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
      response.setStatus("Невозможно зарегистрировать пользователя!");
      return response;
    }

    response.setCode(HttpStatus.BAD_REQUEST.value());
    response.setStatus("Существует пользователь с таким email!");
    return response;
  }
}
