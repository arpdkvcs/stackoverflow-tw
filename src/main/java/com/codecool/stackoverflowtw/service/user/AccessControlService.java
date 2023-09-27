package com.codecool.stackoverflowtw.service.user;

import com.codecool.stackoverflowtw.dao.user.model.Role;

import java.sql.SQLException;
import java.util.Set;

public interface AccessControlService {

  Set<Role> readAllOfUser(long userid) throws SQLException;
  void assignToUser(long userid, Role role) throws SQLException;

  void removeFromUser(long userid, Role role) throws SQLException;

  boolean verifyRoleOfUser(long userid, Role role) throws SQLException;
}
