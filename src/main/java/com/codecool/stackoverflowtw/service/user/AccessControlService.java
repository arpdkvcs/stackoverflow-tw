package com.codecool.stackoverflowtw.service.user;

import com.codecool.stackoverflowtw.dao.user.model.Role;

import java.sql.SQLException;
import java.util.Set;

public interface AccessControlService {

  Set<Role> readAllOfUser(Long userid) throws SQLException;
  void assignToUser(Long userid, Role role) throws SQLException;

  void removeFromUser(Long userid, Role role) throws SQLException;

  boolean verifyRoleOfUser(Long userid, Role role) throws SQLException;
}
