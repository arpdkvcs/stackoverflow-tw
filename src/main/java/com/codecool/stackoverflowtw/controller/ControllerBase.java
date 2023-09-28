package com.codecool.stackoverflowtw.controller;

import com.codecool.stackoverflowtw.controller.dto.user.TokenUserInfoDTO;
import com.codecool.stackoverflowtw.dao.model.Role;
import com.codecool.stackoverflowtw.service.user.AccessControlService;
import com.codecool.stackoverflowtw.service.user.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;
import java.util.Set;


@CrossOrigin(origins = "http://localhost:5000")
@RestController
public abstract class ControllerBase {
  protected final TokenService tokenService;
  protected final AccessControlService accessControlService;
  protected final Logger logger;

  @Autowired
  public ControllerBase(TokenService tokenService, AccessControlService accessControlService) {
    this.tokenService = tokenService;
    this.accessControlService = accessControlService;
    logger = LoggerFactory.getLogger(this.getClass());
  }


  protected ResponseEntity<?> handleOkMessage(String message) {
    logger.info(message);
    return ResponseEntity.ok(Map.of("status", HttpStatus.OK.value(),
      "message", message));
  }

  protected ResponseEntity<?> handleOkData(String logMessage, Object data) {
    logger.info(logMessage);
    return ResponseEntity.ok(Map.of("status", HttpStatus.OK.value(),
      "data", data));
  }

  protected ResponseEntity<?> handleBadRequest(String message, Exception e) {
    if (e != null) {
      logger.error(message, e);
    } else {
      logger.error(message);
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status",
      HttpStatus.BAD_REQUEST.value(), "error", message));
  }

  protected ResponseEntity<?> handleUnauthorized(String message, Exception e) {
    if (e != null) {
      logger.error(message, e);
    } else {
      logger.error(message);
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status",
      HttpStatus.UNAUTHORIZED.value(), "error", message));
  }

  protected void removeSessionCookie(HttpServletResponse response) {
    Cookie cookie = new Cookie("jwt", null);
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/");
    cookie.setMaxAge(0);
    String cookieHeader = String.format(
      "%s; SameSite=None",
      cookie.toString()
    );
    response.addHeader("Set-Cookie", cookieHeader);
  }

  //verifies the token and returns the userinfo if valid
  protected Optional<TokenUserInfoDTO> verifyToken(HttpServletRequest request) {
    try {
      String sessionToken = getCookieValue(request);
      if (sessionToken == null) {
        return Optional.empty();
      }
      return Optional.of(tokenService.verify(sessionToken));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  //can be checked from the token directly (receivedRoles) or from the database with userId
  protected boolean verifyRole(Long userId, Role requiredRole, Set<Role> receivedRoles) {
    try {
      if (receivedRoles != null) {
        return receivedRoles.contains(requiredRole);
      }
      return accessControlService.verifyRoleOfUser(userId, requiredRole);
    } catch (Exception e) {
      return false;
    }
  }

  protected String getCookieValue(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("jwt")) {
          return cookie.getValue();
        }
      }
    }
    return null;
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<?> handleException(Exception e) {
    return handleBadRequest("Invalid data format", e);
  }
}
