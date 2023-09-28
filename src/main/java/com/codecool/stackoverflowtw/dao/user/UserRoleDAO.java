package com.codecool.stackoverflowtw.dao.user;

import com.codecool.stackoverflowtw.dao.model.Role;
import org.springframework.jdbc.CannotGetJdbcConnectionException;

import java.sql.SQLException;
import java.util.Set;

public interface UserRoleDAO {

  Set<Role> readAllOfUser(Long userid) throws SQLException;
  void assignToUser(long userid, Role role)
    throws SQLException, CannotGetJdbcConnectionException;

  void removeFromUser(Long userid, Role role)
    throws SQLException, CannotGetJdbcConnectionException;

  boolean verifyRoleOfUser(Long userid, Role role)
    throws SQLException, CannotGetJdbcConnectionException;
}
