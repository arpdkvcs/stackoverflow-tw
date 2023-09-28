package com.codecool.stackoverflowtw.service.user;

import com.codecool.stackoverflowtw.dao.user.UserRoleDAO;
import com.codecool.stackoverflowtw.dao.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Set;

@Service
public class AccessControlServiceImpl implements AccessControlService {
  private final UserRoleDAO roleDAO;

  @Autowired
  public AccessControlServiceImpl(UserRoleDAO roleDAO) {
    this.roleDAO = roleDAO;
  }


  @Override
  public Set<Role> readAllOfUser(long userid) throws SQLException {
    try {
      return roleDAO.readAllOfUser(userid);
    } catch (CannotGetJdbcConnectionException e) {
      throw new SQLException(e);
    }
  }

  @Override
  public void assignToUser(long userid, Role role)
    throws SQLException {
    try {
      roleDAO.assignToUser(userid, role);
    } catch (CannotGetJdbcConnectionException e) {
      throw new SQLException(e);
    }
  }

  @Override
  public void removeFromUser(long userid, Role role)
    throws SQLException, CannotGetJdbcConnectionException {
    try {
      roleDAO.removeFromUser(userid, role);
    } catch (CannotGetJdbcConnectionException e) {
      throw new SQLException(e);
    }
  }

  @Override
  public boolean verifyRoleOfUser(long userid, Role role)
    throws SQLException, CannotGetJdbcConnectionException {
    try {
      return roleDAO.verifyRoleOfUser(userid, role);
    } catch (CannotGetJdbcConnectionException e) {
      throw new SQLException(e);
    }
  }
}
