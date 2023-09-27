package com.codecool.stackoverflowtw.dao.user;

import com.codecool.stackoverflowtw.dao.user.model.Role;
import org.springframework.jdbc.CannotGetJdbcConnectionException;

import java.sql.SQLException;
import java.util.Set;

public interface UserRoleDAO {

  Set<Role> readAllOfUser(long userid) throws SQLException;
  void assignToUser(long userid, Role role)
    throws SQLException, CannotGetJdbcConnectionException;

  void removeFromUser(long userid, Role role)
    throws SQLException, CannotGetJdbcConnectionException;

  boolean verifyRoleOfUser(long userid, Role role)
    throws SQLException, CannotGetJdbcConnectionException;
}
