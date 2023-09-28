package com.codecool.stackoverflowtw.controller;

import com.codecool.stackoverflowtw.controller.dto.user.TokenUserInfoDTO;
import com.codecool.stackoverflowtw.dao.user.model.Role;
import com.codecool.stackoverflowtw.service.user.AccessControlService;
import com.codecool.stackoverflowtw.service.user.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  protected Optional<TokenUserInfoDTO> verifyToken(String sessionToken) {
    try {
      return Optional.of(tokenService.verify(sessionToken));
    } catch (Exception e){
      return Optional.empty();
    }
  }

  protected boolean verifyRole(long userid, Role role){
    try {
      return accessControlService.verifyRoleOfUser(userid, role);
    } catch (Exception e){
      return false;
    }
  }

  protected ResponseEntity<?> handleBadRequest(String message, Exception e) {
    if (e!=null) {
      logger.error(message, e);
    } else {
      logger.error(message);
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status",
      HttpStatus.BAD_REQUEST.value(), "error", message));
  }

  protected ResponseEntity<?> handleUnauthorized(String message,Exception e) {
    if (e!=null) {
      logger.error(message, e);
    } else {
      logger.error(message);
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status",
      HttpStatus.UNAUTHORIZED.value(), "error", message));
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<?> handleException(Exception e) {
    return handleBadRequest("Invalid data format", e);
  }
}
