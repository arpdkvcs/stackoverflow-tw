package com.codecool.stackoverflowtw.controller;

import com.codecool.stackoverflowtw.controller.dto.user.*;
import com.codecool.stackoverflowtw.service.user.AccessControlService;
import com.codecool.stackoverflowtw.service.user.AuthenticationService;
import com.codecool.stackoverflowtw.service.user.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController extends BaseController {

  private final AuthenticationService authenticationService;

  @Value("${jwt.session-token-expiration}")
  private int sessionTokenExpiration;

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
  public ResponseEntity<?> login(@RequestBody LoginUserDTO loginUserDTO,
                                 HttpServletResponse response) {
    try {
      LoginResponseDTO loginResponse = authenticationService.login(loginUserDTO);
      Cookie cookie = new Cookie("jwt", loginResponse.sessionToken());
      cookie.setHttpOnly(true);
      cookie.setSecure(true);
      cookie.setPath("/");
      cookie.setMaxAge(sessionTokenExpiration);
      String cookieHeader = String.format(
        "%s; SameSite=None",
        cookie.toString()
      );
      response.addHeader("Set-Cookie", cookieHeader);

      Map<String, Object> responseData = new HashMap<>();
      responseData.put("userid", loginResponse.userid());
      responseData.put("username", loginResponse.username());
      responseData.put("roles", loginResponse.roles());

      return ResponseEntity.ok(Map.of("status", HttpStatus.OK.value(),
        "data", responseData));
    } catch (Exception e) {
      return handleUnauthorized("Login failed for user account", e);
    }
  }

  @PostMapping("/authenticateTest")
  public ResponseEntity<?> authenticateTest(@RequestBody long userId,
                                            HttpServletRequest request,
                                            HttpServletResponse response) {
    try {
      Optional<TokenUserInfoDTO> userInfo = verifyToken(request);
      if (userInfo.isPresent()) {
        logger.info(userInfo.get().toString());
        return ResponseEntity.ok(Map.of("status", HttpStatus.OK.value(),
          "message", "ok"));
      } else {
        return handleUnauthorized("Unauthorized", null);
      }
    } catch (Exception e) {
      removeSessionCookie(response);
      return handleBadRequest("error", e);
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(@RequestBody LogoutUserDTO logoutUserDTO,
                                  HttpServletResponse response) {
    try {
      authenticationService.logout(logoutUserDTO);

      removeSessionCookie(response);

      return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", HttpStatus.OK.value(),
        "message", "User account with ID " + logoutUserDTO.userid()
          + " logged out successfully"));
    } catch (Exception e) {
      return handleBadRequest("Logout failed for user account", e);
    }
  }
}
