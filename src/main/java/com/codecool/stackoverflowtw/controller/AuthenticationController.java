package com.codecool.stackoverflowtw.controller;

import com.codecool.stackoverflowtw.controller.dto.user.*;
import com.codecool.stackoverflowtw.service.user.AccessControlService;
import com.codecool.stackoverflowtw.service.user.AuthenticationService;
import com.codecool.stackoverflowtw.service.user.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController extends BaseController {

  private final AuthenticationService authenticationService;

  public AuthenticationController(TokenService tokenService,
                                  AccessControlService accessControlService,
                                  AuthenticationService authenticationService) {
    super(tokenService, accessControlService);
    this.authenticationService = authenticationService;
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
      return handleUnauthorized("Login failed for user account", e);
    }
  }

  @PostMapping("/authenticateTest")
  public ResponseEntity<?> authenticateTest(@RequestBody UserAuthenticationDTO userAuthData) {
    try {
      logger.info(tokenService.verify(userAuthData.sessionToken()).toString());
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
}
