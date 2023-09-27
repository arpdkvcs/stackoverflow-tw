package com.codecool.stackoverflowtw.controller;

import com.codecool.stackoverflowtw.controller.dto.user.*;
import com.codecool.stackoverflowtw.service.user.AuthenticationService;
import com.codecool.stackoverflowtw.service.user.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

  private final AuthenticationService authenticationService;
  private final TokenService tokenService;
  private final Logger logger;

  @Autowired
  public AuthenticationController(AuthenticationService authenticationService, TokenService tokenService) {
    this.authenticationService = authenticationService;
    this.tokenService = tokenService;
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  private ResponseEntity<?> handleBadRequest(String message, Exception e) {
    logger.error(message, e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status",
      HttpStatus.BAD_REQUEST.value(), "error", message));
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody NewUserDTO newUserDTO) {
    try {
      authenticationService.register(newUserDTO);
      return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", HttpStatus.OK.value(),
        "message", "User account created successfully"));
    } catch (Exception e) {
      return handleBadRequest("Failed to create user account", e);
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginUserDTO loginUserDTO) {
    try {
      LoginResponseDTO loginResponse = authenticationService.login(loginUserDTO);
      return ResponseEntity.ok(Map.of("status", HttpStatus.OK.value(),
        "data", loginResponse));
    } catch (Exception e) {
      return handleBadRequest("Login failed for user account", e);
    }
  }

  @PostMapping("/authenticateTest")
  public ResponseEntity<?> authenticateTest(@RequestBody UserAuthenticationDTO userAuthData) {
    try {
      logger.info(tokenService.verify(userAuthData.userid(), userAuthData.sessionToken()).toString());
      return ResponseEntity.ok(Map.of("status", HttpStatus.OK.value(),
        "message", "ok"));
    } catch (Exception e) {
      return handleBadRequest("error", e);
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(@RequestBody LogoutUserDTO logoutUserDTO) {
    try {
      authenticationService.logout(logoutUserDTO);
      return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", HttpStatus.OK.value(),
        "message", "User account with ID " + logoutUserDTO.userid()
          + " logged out successfully"));
    } catch (Exception e) {
      return handleBadRequest("Logout failed for user account", e);
    }
  }

  @ExceptionHandler(Exception.class)
  private ResponseEntity<?> handleException(Exception e) {
    return handleBadRequest("Invalid data format", e);
  }
}
