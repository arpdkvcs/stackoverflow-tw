package com.codecool.stackoverflowtw.dao.user;

import com.codecool.stackoverflowtw.dao.BaseDaoJdbc;
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
public class UserSessionDaoJdbc extends BaseDaoJdbc implements UserSessionDAO {
  public UserSessionDaoJdbc(DataSource dataSource) {
    super(dataSource);
  }

  @Override
  public Set<String> readAllOfUser(long userid)
    throws SQLException, CannotGetJdbcConnectionException {
    Set<String> tokens = new HashSet<>();
    String sql = "SELECT token FROM user_sessions WHERE user_id = ?";

    Connection conn = DataSourceUtils.getConnection(dataSource);
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, userid);

      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        tokens.add(rs.getString("token"));
      }
    } finally {
      releaseConnectionIfNoTransaction(conn, dataSource);
    }
    return tokens;
  }

  @Override
  public void addToUser(long userid, String hashedSessionToken)
    throws SQLException, CannotGetJdbcConnectionException {
    String sql = "INSERT INTO user_sessions (user_id, token) VALUES (?, ?)";

    Connection conn = DataSourceUtils.getConnection(dataSource);
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, userid);
      ps.setString(2, hashedSessionToken);

      int affectedRows = ps.executeUpdate();
      if (affectedRows == 0) {
        throw new SQLException("Failed to add session token to user with ID " + userid);
      }
    } finally {
      releaseConnectionIfNoTransaction(conn, dataSource);
    }
  }

  @Override
  public void removeFromUser(long userid, String hashedSessionToken)
    throws SQLException, CannotGetJdbcConnectionException {
    String sql = "DELETE FROM user_sessions WHERE user_id = ? AND token = ?";

    Connection conn = DataSourceUtils.getConnection(dataSource);
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, userid);
      ps.setString(2, hashedSessionToken);

      int affectedRows = ps.executeUpdate();
      if (affectedRows == 0) {
        throw new SQLException("Failed to remove session token from user with ID " + userid);
      }
    } finally {
      releaseConnectionIfNoTransaction(conn, dataSource);
    }
  }

  @Override
  public void removeAllFromUser(long userid) throws SQLException, CannotGetJdbcConnectionException {
    String sql = "DELETE FROM user_sessions WHERE user_id = ?";

    Connection conn = DataSourceUtils.getConnection(dataSource);
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, userid);

      int affectedRows = ps.executeUpdate();
      if (affectedRows == 0) {
        throw new SQLException("Failed to remove all session tokens from user with ID " + userid);
      }
    } finally {
      releaseConnectionIfNoTransaction(conn, dataSource);
    }
  }
}