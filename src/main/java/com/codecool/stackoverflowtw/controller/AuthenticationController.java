package com.codecool.stackoverflowtw.controller;

import com.codecool.stackoverflowtw.controller.dto.user.*;
import com.codecool.stackoverflowtw.dao.model.Role;
import com.codecool.stackoverflowtw.service.user.AccessControlService;
import com.codecool.stackoverflowtw.service.user.AuthenticationService;
import com.codecool.stackoverflowtw.service.user.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController extends ControllerBase {

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
      return handleOkMessage("UserModel account created successfully");
    } catch (Exception e) {
      return handleBadRequest("Failed to create user account", e);
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginUserDTO loginUserDTO,
                                 HttpServletResponse response) {
    try {
      LoginResponseDTO loginResponse = authenticationService.login(loginUserDTO);

      String cookieValue = String.format(
        "jwt=%s; Path=/; HttpOnly; Secure; SameSite=None; Max-Age=%d",
        loginResponse.sessionToken(),
        sessionTokenExpiration
      );
      response.addHeader("Set-Cookie", cookieValue);

      Map<String, Object> responseData = new HashMap<>();
      responseData.put("userid", loginResponse.userid());
      responseData.put("username", loginResponse.username());
      responseData.put("roles", loginResponse.roles());

      return handleOkData("UserModel account logged in successfully", responseData);
    } catch (Exception e) {
      return handleUnauthorized("Login failed for user account", e);
    }
  }

  @PostMapping("/authenticateTest")
  public ResponseEntity<?> authenticateTest(HttpServletRequest request,
                                            HttpServletResponse response) {
    try {
      Optional<TokenUserInfoDTO> userInfo = verifyToken(request);
      if (userInfo.isPresent()) {
        long userId = userInfo.get().userid();
        //create xyzDTO with this ID, call service method etc

        String username = userInfo.get().username();
        Set<Role> roles = userInfo.get().roles();

        //can verify role from received roles, or if null, from the database
        boolean hasRequiredRoleInToken = verifyRole(userId, Role.USER, roles);
        boolean hasRequiredRoleInDb = verifyRole(userId, Role.USER, null);

        return handleOkMessage("user ID: " + userId + ", username:" + username
          + ", roles:" + roles
          + ", has required role in token: " + hasRequiredRoleInToken
          + ", has required role in database: " + hasRequiredRoleInDb);
      } else {
        return handleUnauthorized("Unauthorized", null);
      }
    } catch (Exception e) {
      removeSessionCookie(response);
      return handleBadRequest("error", e);
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(HttpServletRequest request,
                                  HttpServletResponse response) {
    try {
      Optional<TokenUserInfoDTO> userInfo = verifyToken(request);
      if (userInfo.isPresent()) {
        authenticationService.logout(new LogoutUserDTO(userInfo.get().userid(),
          getCookieValue(request)));
      }

      removeSessionCookie(response);

      return handleOkMessage("UserModel account logged out successfully");
    } catch (Exception e) {
      removeSessionCookie(response);
      return handleBadRequest("Logout failed for user account", e);
    }
  }
}
