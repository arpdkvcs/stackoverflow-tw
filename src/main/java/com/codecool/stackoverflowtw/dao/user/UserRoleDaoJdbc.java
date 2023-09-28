package com.codecool.stackoverflowtw.dao.user;

import com.codecool.stackoverflowtw.dao.BaseDaoJdbc;
import com.codecool.stackoverflowtw.dao.model.Role;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Repository
public class UserRoleDaoJdbc extends BaseDaoJdbc implements UserRoleDAO {
  public UserRoleDaoJdbc(DataSource dataSource) {
    super(dataSource);
  }

  @Override
  public Set<Role> readAllOfUser(long userid)
    throws SQLException, CannotGetJdbcConnectionException {
    Set<Role> roles = new HashSet<>();
    String sql = "SELECT r.name " +
      "FROM roles r " +
      "JOIN user_roles ur ON r.id = ur.role_id " +
      "WHERE ur.user_id = ?";

    Connection conn = DataSourceUtils.getConnection(dataSource);
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, userid);

      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        roles.add(Role.valueOf(rs.getString("name")));
      }
    } finally {
      releaseConnectionIfNoTransaction(conn);
    }
    return roles;
  }

  @Override
  public void assignToUser(long userid, Role role)
    throws SQLException, CannotGetJdbcConnectionException {
    String sql = "INSERT INTO user_roles (user_id, role_id)" +
      " SELECT ?, id FROM roles WHERE name = ?";

    Connection conn = DataSourceUtils.getConnection(dataSource);
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, userid);
      ps.setString(2, role.toString());

      int affectedRows = ps.executeUpdate();
      if (affectedRows == 0) {
        throw new SQLException("Failed to assign role " + role + " to user with ID "
          + userid);
      }
    } finally {
      releaseConnectionIfNoTransaction(conn);
    }
  }

  @Override
  public void removeFromUser(long userid, Role role)
    throws SQLException, CannotGetJdbcConnectionException {
    String sql = "DELETE FROM user_roles WHERE user_id = ? " +
      "AND role_id IN (SELECT id FROM roles WHERE name = ?)";

    Connection conn = DataSourceUtils.getConnection(dataSource);
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, userid);
      ps.setString(2, role.toString());
      int affectedRows = ps.executeUpdate();

      if (affectedRows == 0) {
        throw new SQLException("Failed to remove role " + role + " from user with ID "
          + userid);
      }
    } finally {
      releaseConnectionIfNoTransaction(conn);
    }
  }

  @Override
  public boolean verifyRoleOfUser(long userid, Role role)
    throws SQLException, CannotGetJdbcConnectionException {
    String sql = "SELECT r.name " +
      "FROM roles r " +
      "JOIN user_roles ur ON r.id = ur.role_id " +
      "WHERE ur.user_id = ? AND r.name = ?";

    Connection conn = DataSourceUtils.getConnection(dataSource);
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, userid);
      ps.setString(2, role.toString());

      ResultSet rs = ps.executeQuery();
      return rs.next();
    } finally {
      releaseConnectionIfNoTransaction(conn);
    }
  }
}
