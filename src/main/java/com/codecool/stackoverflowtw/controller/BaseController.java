package com.codecool.stackoverflowtw.controller;

import com.codecool.stackoverflowtw.controller.dto.user.TokenUserInfoDTO;
import com.codecool.stackoverflowtw.dao.user.model.Role;
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


@CrossOrigin(origins = "http://localhost:5000")
@RestController
public abstract class BaseController {
  protected final TokenService tokenService;
  protected final AccessControlService accessControlService;
  protected final Logger logger;

  @Autowired
  public BaseController(TokenService tokenService, AccessControlService accessControlService) {
    this.tokenService = tokenService;
    this.accessControlService = accessControlService;
    logger = LoggerFactory.getLogger(this.getClass());
  }

  private String getCookieAsString(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("jwt")) {
          return String.format("%s; Value=%s; HttpOnly=%b; Secure=%b; Path=%s; Max-Age=%d; SameSite=None",
            cookie.getName(), cookie.getValue(), cookie.isHttpOnly(),
            cookie.getSecure(), cookie.getPath(), cookie.getMaxAge());
        }
      }
    }
    return null;
  }

  protected Optional<TokenUserInfoDTO> verifyToken(HttpServletRequest request) {
    try {
      String sessionToken = getCookieAsString(request);
      if (sessionToken == null) {
        return Optional.empty();
      }
      return Optional.of(tokenService.verify(sessionToken));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  protected boolean verifyRole(long userid, Role role) {
    try {
      return accessControlService.verifyRoleOfUser(userid, role);
    } catch (Exception e) {
      return false;
    }
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

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<?> handleException(Exception e) {
    return handleBadRequest("Invalid data format", e);
  }
}
