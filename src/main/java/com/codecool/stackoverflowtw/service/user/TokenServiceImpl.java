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
  private JWTVerifier verifier;
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

  private Map<String, Object> createUserInfoMap(TokenUserInfoDTO userInfo) {
    Map<String, Object> userInfoMap = new HashMap<>();
    userInfoMap.put("userid", userInfo.userid());
    userInfoMap.put("username", userInfo.username());

    userInfoMap.put("roles",
      userInfo.roles().stream().map(role -> role.toString()).collect(Collectors.toSet()));

    return userInfoMap;
  }

  private TokenUserInfoDTO parseUserInfoMap(Map<String, Object> userInfoMap) {
    Long userid = ((Number) userInfoMap.get("userid")).longValue();
    String username = userInfoMap.get("username").toString();

    Set<String> rolesStrings = new HashSet<>((Collection<String>) userInfoMap.get("roles"));
    Set<Role> roles = rolesStrings.stream()
      .map(role -> Role.valueOf(role))
      .collect(Collectors.toSet());

    return new TokenUserInfoDTO(userid, username, roles);
  }


  @Override
  public String sign(TokenUserInfoDTO userInfo) throws RuntimeException {
    try {
      JWTCreator.Builder builder = JWT.create().withClaim("UserInfo", createUserInfoMap(userInfo))
        .withExpiresAt(new Date(System.currentTimeMillis() + sessionTokenExpiration));

      return builder.sign(sessionTokenAlgorithm);
    } catch (Exception e) {
      throw new RuntimeException("Failed to sign session token", e);
    }
  }

  @Override
  @Transactional(rollbackFor = JWTVerificationException.class)
  public TokenUserInfoDTO verify(long userid, String rawSessionToken) throws RuntimeException {
    try {
      DecodedJWT jwt = verifier.verify(rawSessionToken);

      Set<String> hashedSessionTokens = userSessionDAO.readAllOfUser(userid);
      String matchingToken = null;
      for (String hashedToken : hashedSessionTokens) {
        if (BCrypt.checkpw(rawSessionToken, hashedToken)) {
          matchingToken = hashedToken;
        }
      }
      if (matchingToken == null) {
        userSessionDAO.removeAllFromUser(userid);
        throw new RuntimeException("Invalid session token found at user with ID " + userid
          + ", clearing all sessions");
      }

      Map<String, Object> userInfoString = jwt.getClaim("UserInfo").asMap();
      return parseUserInfoMap(userInfoString);
    } catch (Exception e) {
      throw new RuntimeException("Invalid session token", e);
    }
  }

  @Override
  public Set<String> readAllOfUser(long userid) throws SQLException {
    try {
      return userSessionDAO.readAllOfUser(userid);
    } catch (CannotGetJdbcConnectionException e) {
      throw new SQLException(e);
    }
  }

  @Override
  public void addToUser(long userid, String rawRefreshToken) throws SQLException {
    try {
      userSessionDAO.addToUser(userid, BCrypt.hashpw(rawRefreshToken, BCrypt.gensalt(3)));
    } catch (CannotGetJdbcConnectionException e) {
      throw new SQLException(e);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void removeFromUser(long userid, String rawSessionToken)
    throws SQLException {
    try {
      Set<String> hashedSessionTokens = userSessionDAO.readAllOfUser(userid);
      String hashedTokenToRemove = null;
      for (String hashedToken : hashedSessionTokens) {
        if (BCrypt.checkpw(rawSessionToken, hashedToken)) {
          hashedTokenToRemove = hashedToken;
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
  public void removeAllFromUser(long userid) throws SQLException {
    try {
      userSessionDAO.removeAllFromUser(userid);
    } catch (CannotGetJdbcConnectionException e) {
      throw new SQLException(e);
    }
  }
}
