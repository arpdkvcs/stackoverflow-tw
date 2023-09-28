package com.codecool.stackoverflowtw.service.user;

import com.codecool.stackoverflowtw.controller.dto.user.TokenUserInfoDTO;

import java.sql.SQLException;
import java.util.Set;

public interface TokenService {
  String sign(TokenUserInfoDTO userInfo) throws RuntimeException;

  TokenUserInfoDTO verify(String rawSessionToken) throws RuntimeException;

  Set<String> readAllOfUser(Long userid) throws SQLException;

  void addToUser(Long userid, String rawSessionToken) throws SQLException;

  void removeFromUser(Long userid, String rawSessionToken) throws SQLException;

  void removeAllFromUser(Long userid) throws SQLException;
}
