package com.codecool.stackoverflowtw.dao.user;

import org.springframework.jdbc.CannotGetJdbcConnectionException;

import java.sql.SQLException;
import java.util.Set;

public interface UserSessionDAO {
  Set<String> readAllOfUser(long userid)
    throws SQLException, CannotGetJdbcConnectionException;

  void addToUser(long userid, String hashedSessionToken)
    throws SQLException, CannotGetJdbcConnectionException;

  void removeFromUser(long userid, String hashedSessionToken)
    throws SQLException, CannotGetJdbcConnectionException;

  void removeAllFromUser(long userid)
    throws SQLException, CannotGetJdbcConnectionException;
}
