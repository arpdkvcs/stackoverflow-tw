package com.codecool.stackoverflowtw.dao.user;

import com.codecool.stackoverflowtw.dao.model.UserModel;
import org.springframework.jdbc.CannotGetJdbcConnectionException;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

public interface UserDAO {
  Set<UserModel> readAll() throws SQLException, CannotGetJdbcConnectionException;

  Optional<UserModel> readByUsername(String username) throws SQLException,
    CannotGetJdbcConnectionException;

  Optional<UserModel> readById(Long id) throws SQLException, CannotGetJdbcConnectionException;

  void create(String username, String hashedPassword)
    throws SQLException, CannotGetJdbcConnectionException;

  void update(Long id, String username, String hashedPassword)
    throws SQLException, CannotGetJdbcConnectionException;

  void delete(Long id) throws SQLException, CannotGetJdbcConnectionException;

  String getUsernameById(Long userId) throws SQLException;
}
