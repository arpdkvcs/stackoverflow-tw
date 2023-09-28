package com.codecool.stackoverflowtw.dao.user;

import com.codecool.stackoverflowtw.dao.user.model.User;
import org.springframework.jdbc.CannotGetJdbcConnectionException;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

public interface UserDAO {
  Set<User> readAll() throws SQLException, CannotGetJdbcConnectionException;

  Optional<User> readByUsername(String username) throws SQLException,
    CannotGetJdbcConnectionException;

  Optional<User> readById(long id) throws SQLException, CannotGetJdbcConnectionException;

  void create(String username, String hashedPassword)
    throws SQLException, CannotGetJdbcConnectionException;

  void update(long id, String username, String hashedPassword)
    throws SQLException, CannotGetJdbcConnectionException;

  void delete(long id) throws SQLException, CannotGetJdbcConnectionException;

  String getUsernameById(long userId) throws SQLException;
}
