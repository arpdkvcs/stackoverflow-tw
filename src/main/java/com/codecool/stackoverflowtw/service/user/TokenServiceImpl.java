package com.codecool.stackoverflowtw.service.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.codecool.stackoverflowtw.controller.dto.user.TokenUserInfoDTO;
import com.codecool.stackoverflowtw.dao.user.UserSessionDAO;
import com.codecool.stackoverflowtw.dao.user.model.Role;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TokenServiceImpl implements TokenService {
  private final Algorithm sessionTokenAlgorithm;
  private final JWTVerifier verifier;
  private final long sessionTokenExpiration;
  private final UserSessionDAO userSessionDAO;

  @Autowired
  public TokenServiceImpl(@Value("${jwt.session-token-secret}") String sessionTokenSecret,
                          @Value("${jwt.session-token-expiration}") long sessionTokenExpiration,
                          UserSessionDAO userSessionDAO) {
    this.sessionTokenExpiration = sessionTokenExpiration;
    this.sessionTokenAlgorithm = Algorithm.HMAC256(sessionTokenSecret);
    this.verifier = JWT.require(this.sessionTokenAlgorithm).build();
    this.userSessionDAO = userSessionDAO;
  }

  @Override
  public String sign(TokenUserInfoDTO userInfo) throws RuntimeException {
    try {
      JWTCreator.Builder builder = JWT.create()
        .withClaim("userid", userInfo.userid())
        .withClaim("username", userInfo.username())
        .withArrayClaim("roles",
          userInfo.roles().stream()
            .map(Enum::toString)
            .toArray(String[]::new))
        .withClaim("uuid", UUID.randomUUID().toString())
        .withExpiresAt(new Date(System.currentTimeMillis() + sessionTokenExpiration));

      return builder.sign(sessionTokenAlgorithm);
    } catch (Exception e) {
      throw new RuntimeException("Failed to sign session token", e);
    }
  }

  @Override
  @Transactional(rollbackFor = JWTVerificationException.class)
  public TokenUserInfoDTO verify(String rawSessionToken) throws RuntimeException {
    try {
      DecodedJWT jwt = verifier.verify(rawSessionToken);
      Long userId = jwt.getClaim("userid").asLong();
      String username = jwt.getClaim("username").asString();
      String[] rolesArray = jwt.getClaim("roles").asArray(String.class);
      Set<Role> roles = Arrays.stream(rolesArray)
        .map(roleStr -> Role.valueOf(roleStr))
        .collect(Collectors.toSet());

      Set<String> hashedSessionTokens = userSessionDAO.readAllOfUser(userId);
      String matchingToken = null;
      for (String hashedToken : hashedSessionTokens) {
        if (BCrypt.checkpw(rawSessionToken, hashedToken)) {
          matchingToken = hashedToken;
          break;
        }
      }
      if (matchingToken == null) {
        userSessionDAO.removeAllFromUser(userId);
        throw new RuntimeException("Invalid session token found at user with ID " + userId
          + ", clearing all sessions");
      }

      return new TokenUserInfoDTO(userId, username, roles);
    } catch (Exception e) {
      throw new RuntimeException("Invalid session token", e);
    }
  }


  @Override
  public Set<String> readAllOfUser(Long userid) throws SQLException {
    try {
      return userSessionDAO.readAllOfUser(userid);
    } catch (CannotGetJdbcConnectionException e) {
      throw new SQLException(e);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void addToUser(Long userid, String rawRefreshToken) throws SQLException {
    try {
      userSessionDAO.addToUser(userid, BCrypt.hashpw(rawRefreshToken, BCrypt.gensalt(4)));
    } catch (CannotGetJdbcConnectionException e) {
      throw new SQLException(e);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void removeFromUser(Long userid, String rawSessionToken)
    throws SQLException {
    try {
      Set<String> hashedSessionTokens = userSessionDAO.readAllOfUser(userid);
      String hashedTokenToRemove = null;
      for (String hashedToken : hashedSessionTokens) {
        if (BCrypt.checkpw(rawSessionToken, hashedToken)) {
          hashedTokenToRemove = hashedToken;
          break;
        }
      }
      if (hashedTokenToRemove != null) {
        userSessionDAO.removeFromUser(userid, hashedTokenToRemove);
      } else {
        throw new RuntimeException("Token to remove not found at user with ID " + userid);
      }
    } catch (Exception e) {
      throw new SQLException(e);
    }
  }

  @Override
  public void removeAllFromUser(Long userid) throws SQLException {
    try {
      userSessionDAO.removeAllFromUser(userid);
    } catch (CannotGetJdbcConnectionException e) {
      throw new SQLException(e);
    }
  }
}
