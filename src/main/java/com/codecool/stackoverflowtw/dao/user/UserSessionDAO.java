package com.codecool.stackoverflowtw.dao.user;

import org.springframework.jdbc.CannotGetJdbcConnectionException;

import java.sql.SQLException;
import java.util.Set;

public interface UserSessionDAO {
  Set<String> readAllOfUser(Long userid)
    throws SQLException, CannotGetJdbcConnectionException;

  void addToUser(Long userid, String hashedSessionToken)
    throws SQLException, CannotGetJdbcConnectionException;

  void removeFromUser(Long userid, String hashedSessionToken)
    throws SQLException, CannotGetJdbcConnectionException;

  void removeAllFromUser(Long userid)
    throws SQLException, CannotGetJdbcConnectionException;
}
