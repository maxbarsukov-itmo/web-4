package ru.itmo.web.lab4.attempts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;

import ru.itmo.web.lab4.attempts.dto.AttemptDtoMapper;
import ru.itmo.web.lab4.attempts.dto.InvalidDtoResponse;
import ru.itmo.web.lab4.users.User;
import ru.itmo.web.lab4.users.UserService;
import ru.itmo.web.lab4.attempts.dto.AttemptDto;
import ru.itmo.web.lab4.common.security.jwt.JwtUtils;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api/attempts")
public class AttemptController {
  private final UserService userService;
  private final AttemptService attemptService;
  private final JwtUtils jwt;
  private final AttemptDtoMapper mapper;

  @Autowired
  public AttemptController(AttemptService attemptService, UserService userService, JwtUtils jwt, AttemptDtoMapper mapper) {
    this.userService = userService;
    this.attemptService = attemptService;
    this.jwt = jwt;
    this.mapper = mapper;
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  private ResponseEntity<Object> addPoint(@Valid @RequestBody AttemptDto attemptDto, HttpServletRequest request) {
    if (!attemptDto.validate()) return invalidDto();

    var attempt = mapper.toEntity(attemptDto);
    attemptService.addAttemptByCreator(attempt, getCurrentUser(request));
    return new ResponseEntity<>(attempt, HttpStatus.CREATED);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  private ResponseEntity<List<Attempt>> getPoints(HttpServletRequest request) {
    return ResponseEntity.ok(attemptService.getAllAttemptByCreatorId(getCurrentUser(request).getId()));
  }

  @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  private ResponseEntity<String> deletePoints(HttpServletRequest request) {
    attemptService.deleteByCreator(getCurrentUser(request));
    // TODO: send mail all your attempts were deleted
    return new ResponseEntity<>("Все ваши точки удалены", HttpStatus.OK);
  }

  private User getCurrentUser(HttpServletRequest request) {
    return userService.findByUsername(jwt.getUsername(jwt.resolveToken(request)));
  }

  private ResponseEntity<Object> invalidDto() {
    return new ResponseEntity<>(new InvalidDtoResponse(), HttpStatus.UNPROCESSABLE_ENTITY);
  }
}
