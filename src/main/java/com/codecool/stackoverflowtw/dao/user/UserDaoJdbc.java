package com.codecool.stackoverflowtw.dao.user;

import com.codecool.stackoverflowtw.dao.BaseDaoJdbc;
import com.codecool.stackoverflowtw.dao.model.Role;
import com.codecool.stackoverflowtw.dao.model.UserModel;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Repository
public class UserDaoJdbc extends BaseDaoJdbc implements UserDAO {
  public UserDaoJdbc(DataSource dataSource) {
    super(dataSource);
  }

  private Set<UserModel> getUsersSet(ResultSet rs) throws SQLException {
    //TODO: ???
    Set<UserModel> users = new HashSet<>();
    while (rs.next()) {
      long userId = rs.getLong("id");
      UserModel user = users.stream().filter(u -> u.getId() == userId).findFirst().orElse(null);

      if (user == null) {
        user = new UserModel(
          userId,
          rs.getString("username"),
          rs.getString("password"),
          new HashSet<>(),
          new HashSet<>()
        );
        users.add(user);
      }

      String roleName = rs.getString("role_name");
      if (roleName != null) {
        user.addRole(Role.valueOf(roleName));
      }

      String token = rs.getString("token");
      if (token != null) {
        user.addHashedSessionToken(token);
      }
    }
    return users;
  }

  private Optional<UserModel> getUserObject(ResultSet rs) throws SQLException {
    //TODO: ???
    UserModel user = null;
    while (rs.next()) {
      if (user == null) {
        user = new UserModel(
          rs.getLong("id"),
          rs.getString("username"),
          rs.getString("password"),
          new HashSet<>(),
          new HashSet<>()
        );
      }
      String roleName = rs.getString("role_name");
      if (roleName != null) {
        user.addRole(Role.valueOf(roleName));
      }
      String token = rs.getString("token");
      if (token != null) {
        user.addHashedSessionToken(token);
      }
    }
    if (user != null) {
      return Optional.of(user);
    }
    return Optional.empty();
  }

  @Override
  public Set<UserModel> readAll() throws SQLException, CannotGetJdbcConnectionException {
    String sql =
      "SELECT u.id, u.username, u.password, r.name AS role_name, us.token AS token " +
        "FROM users u " +
        "LEFT JOIN user_roles ur ON u.id = ur.user_id " +
        "LEFT JOIN roles r ON ur.role_id = r.id " +
        "LEFT JOIN user_sessions us ON u.id = us.user_id;";

    Connection conn = DataSourceUtils.getConnection(dataSource);
    try (
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery()) {

      return getUsersSet(rs);
    } finally {
      releaseConnectionIfNoTransaction(conn);
    }
  }

  @Override
  public Optional<UserModel> readByUsername(String username)
    throws SQLException, CannotGetJdbcConnectionException {
    String sql = "SELECT u.id, u.username, u.password, r.name AS role_name, us.token AS token " +
      "FROM users u " +
      "LEFT JOIN user_roles ur ON u.id = ur.user_id " +
      "LEFT JOIN roles r ON ur.role_id = r.id " +
      "LEFT JOIN user_sessions us ON u.id = us.user_id " +
      "WHERE u.username = ?;";

    Connection conn = DataSourceUtils.getConnection(dataSource);
    try (
      PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, username);

      ResultSet rs = ps.executeQuery();
      return getUserObject(rs);
    } finally {
      releaseConnectionIfNoTransaction(conn);
    }
  }

  @Override
  public Optional<UserModel> readById(Long id) throws SQLException, CannotGetJdbcConnectionException {
    String sql = "SELECT u.id, u.username, u.password, r.name AS role_name, us.token AS token " +
      "FROM users u " +
      "LEFT JOIN user_roles ur ON u.id = ur.user_id " +
      "LEFT JOIN roles r ON ur.role_id = r.id " +
      "LEFT JOIN user_sessions us ON u.id = us.user_id " +
      "WHERE u.id = ?;";

    Connection conn = DataSourceUtils.getConnection(dataSource);
    try (
      PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, id);

      ResultSet rs = ps.executeQuery();
      return getUserObject(rs);
    } finally {
      releaseConnectionIfNoTransaction(conn);
    }
  }

  @Override
  public void create(String username, String hashedPassword)
    throws SQLException, CannotGetJdbcConnectionException {
    String sql = "INSERT INTO users (username, password) VALUES (?, ?);";

    Connection conn = DataSourceUtils.getConnection(dataSource);
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, username);
      ps.setString(2, hashedPassword);

      int affectedRows = ps.executeUpdate();
      if (affectedRows == 0) {
        throw new SQLException("Failed to create user" + username);
      }
    } finally {
      releaseConnectionIfNoTransaction(conn);
    }
  }

  @Override
  public void update(Long id, String username, String hashedPassword)
    throws SQLException, CannotGetJdbcConnectionException {
    String sql = "UPDATE users SET username = ?, password = ? WHERE id = ?;";

    Connection conn = DataSourceUtils.getConnection(dataSource);
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, username);
      ps.setString(2, hashedPassword);
      ps.setLong(3, id);

      int affectedRows = ps.executeUpdate();
      if (affectedRows == 0) {
        throw new SQLException("Failed to update user" + username);
      }
    } finally {
      releaseConnectionIfNoTransaction(conn);
    }

  }

  @Override
  public void delete(Long id) throws SQLException, CannotGetJdbcConnectionException {
    String sql = "DELETE FROM users WHERE id = ?;";
    Connection conn = DataSourceUtils.getConnection(dataSource);
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, id);

      int affectedRows = ps.executeUpdate();
      if (affectedRows == 0) {
        throw new SQLException("Failed to delete user with ID " + id);
      }
    } finally {
      releaseConnectionIfNoTransaction(conn);
    }
  }

  @Override
  public String getUsernameById(Long userId)
    throws SQLException, CannotGetJdbcConnectionException {
    String sql = "SELECT username FROM users WHERE id = ?;";

    Connection conn = DataSourceUtils.getConnection(dataSource);
    try (
      PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, userId);

      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        return rs.getString("username");
      } else {
        throw new SQLException("User with ID " + userId + " not found");
      }
    } finally {
      releaseConnectionIfNoTransaction(conn);
    }
  }
}
