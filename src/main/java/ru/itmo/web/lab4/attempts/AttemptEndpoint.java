package ru.itmo.web.lab4.attempts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import ru.itmo.web.lab4.*;
import ru.itmo.web.lab4.attempts.dto.AttemptDtoMapper;
import ru.itmo.web.lab4.common.security.jwt.JwtUtils;
import ru.itmo.web.lab4.notifications.NotificationService;
import ru.itmo.web.lab4.users.User;
import ru.itmo.web.lab4.users.UserService;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import static ru.itmo.web.lab4.common.config.WebServiceConfig.NAMESPACE_URI;

@Endpoint
public class AttemptEndpoint {
  private final UserService userService;
  private final NotificationService notificationService;
  private final AttemptService attemptService;
  private final JwtUtils jwt;
  private final AttemptDtoMapper mapper;

  @Autowired
  public AttemptEndpoint(
    AttemptService attemptService,
    NotificationService notificationService,
    UserService userService,
    JwtUtils jwt,
    AttemptDtoMapper mapper
  ) {
    this.userService = userService;
    this.notificationService = notificationService;
    this.attemptService = attemptService;
    this.jwt = jwt;
    this.mapper = mapper;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addPointRequest")
  @ResponsePayload
  public AddPointResponse addPoint(@RequestPayload AddPointRequest request) {
    var response = new AddPointResponse();

    var attemptDto = request.getAttemptDto();
    var attemptDtoMapped = new ru.itmo.web.lab4.attempts.dto.AttemptDto(
      attemptDto.getX(), attemptDto.getY(), attemptDto.getR()
    );

    if (!attemptDtoMapped.validate()) {
      response.setCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
      response.setStatus("Невалидные координаты!");
      return response;
    }

    var attemptEntity = mapper.toEntity(attemptDtoMapped);
    attemptService.addAttemptByCreator(attemptEntity, getCurrentUser(request.getToken()));

    response.setCode(HttpStatus.CREATED.value());
    response.setStatus("Точка успешно добавлена");
    response.setAttempt(entityToSoap(attemptEntity));
    return response;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getPointsRequest")
  @ResponsePayload
  public GetPointsResponse getPoints(@RequestPayload GetPointsRequest request) {
    var response = new GetPointsResponse();
    var user = getCurrentUser(request.getToken());

    if (user != null) {
      var attempts = attemptService.getAllAttemptByCreatorId(user.getId());

      var soapAttempts = new GetPointsResponse.Attempts();
      soapAttempts.getAttempt().addAll(attempts.stream().map(this::entityToSoap).toList());

      response.setCode(HttpStatus.OK.value());
      response.setStatus("Точки успешно получены.");
      response.setAttempts(soapAttempts);
      return response;
    }

    response.setCode(HttpStatus.UNAUTHORIZED.value());
    response.setStatus("Пользователь на аутентифицирован!");
    return response;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deletePointsRequest")
  @ResponsePayload
  public DeletePointsResponse deletePoints(@RequestPayload DeletePointsRequest request) {
    var response = new DeletePointsResponse();
    var user = getCurrentUser(request.getToken());

    if (user != null) {
      attemptService.deleteByCreator(user);
      notificationService.infoDeleting(user.getEmail());

      response.setCode(HttpStatus.OK.value());
      response.setStatus("Все ваши точки удалены.");
      return response;
    }

    response.setCode(HttpStatus.UNAUTHORIZED.value());
    response.setStatus("Пользователь на аутентифицирован!");
    return response;
  }

  private User getCurrentUser(String token) {
    return userService.findByEmail(jwt.getEmail(token));
  }

  private ru.itmo.web.lab4.Attempt entityToSoap(ru.itmo.web.lab4.attempts.Attempt attemptEntity) {
    GregorianCalendar gc = new GregorianCalendar();
    gc.setTime(attemptEntity.getCreatedAt());

    var user = attemptEntity.getCreator();
    var creator = new Creator();
    creator.setId(user.getId());
    creator.setEmail(user.getEmail());

    var attemptSoap = new ru.itmo.web.lab4.Attempt();
    try {
      attemptSoap.setId(attemptEntity.getId());
      attemptSoap.setX(attemptEntity.getX());
      attemptSoap.setY(attemptEntity.getY());
      attemptSoap.setR(attemptEntity.getR());
      attemptSoap.setResult(attemptEntity.isResult());
      attemptSoap.setCreatedAt(DatatypeFactory.newInstance().newXMLGregorianCalendar(gc));
      attemptSoap.setCreator(creator);
      return attemptSoap;
    } catch (DatatypeConfigurationException e) {
      return attemptSoap;
    }
  }
}
