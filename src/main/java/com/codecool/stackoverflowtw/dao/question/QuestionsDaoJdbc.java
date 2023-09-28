package com.codecool.stackoverflowtw.dao.question;

import com.codecool.stackoverflowtw.dao.BaseDaoJdbc;
import com.codecool.stackoverflowtw.dao.model.QuestionModel;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Repository
public class QuestionsDaoJdbc extends BaseDaoJdbc implements QuestionsDAO {
  @Autowired
  public QuestionsDaoJdbc(DataSource dataSource) {
    super(dataSource);
  }

  @Override
  public int create(QuestionModel questionModel)
    throws SQLException, CannotGetJdbcConnectionException {
    String sql = "INSERT INTO questions (user_id, title, content) VALUES (?, ?, ?) RETURNING id;";
    Connection conn = DataSourceUtils.getConnection(dataSource);

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, questionModel.getUserId());
      pstmt.setString(2, questionModel.getTitle());
      pstmt.setString(3, questionModel.getContent());

      ResultSet resultSet = pstmt.executeQuery();

      if (!resultSet.next()) {
        throw new SQLException("No record has been created in questions table.");
      } else {
        return resultSet.getInt("id");
      }

    } finally {
      releaseConnectionIfNoTransaction(conn);
    }
  }

  @Override
  public Set<QuestionModel> readAll()
    throws SQLException, CannotGetJdbcConnectionException {
    String sql = "SELECT * FROM questions;";
    Connection conn = DataSourceUtils.getConnection(dataSource);

    try (PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

      Set<QuestionModel> questionModels = new HashSet<>();

      while (rs.next()) {
        questionModels.add(getQuestionModel(rs));
      }
      return questionModels;

    } finally {
      releaseConnectionIfNoTransaction(conn);
    }
  }

  @Override
  public QuestionModel readById(Long questionId)
    throws SQLException, CannotGetJdbcConnectionException {
    String sql = "SELECT * FROM questions WHERE id = ?;";
    Connection conn = DataSourceUtils.getConnection(dataSource);

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, questionId);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        return getQuestionModel(rs);
      }

    } finally {
      releaseConnectionIfNoTransaction(conn);
    }
    return null;
  }

  @Override
  public Set<QuestionModel> readByTitle(String searchQuery)
    throws SQLException, CannotGetJdbcConnectionException {
    String sql = "SELECT * FROM questions " +
      "WHERE title ILIKE '%" +
      searchQuery +
      "%';";
    Connection conn = DataSourceUtils.getConnection(dataSource);

    try (PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {
      Set<QuestionModel> questionModels = new HashSet<>();

      while (rs.next()) {
        questionModels.add(getQuestionModel(rs));
      }
      return questionModels;

    } finally {
      releaseConnectionIfNoTransaction(conn);
    }
  }

  @Override
  public void update(QuestionModel questionModel)
    throws SQLException, CannotGetJdbcConnectionException {
    String sql = "UPDATE questions SET " +
      "title = ?, " +
      "content = ? " +
      "WHERE id = ?;";
    Connection conn = DataSourceUtils.getConnection(dataSource);

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, questionModel.getTitle());
      pstmt.setString(2, questionModel.getContent());
      pstmt.setLong(3, questionModel.getId());

      int affectedRows = pstmt.executeUpdate();

      if (affectedRows == 0) {
        throw new SQLException("Couldn't update question in questions table.");
      }

    } finally {
      releaseConnectionIfNoTransaction(conn);
    }
  }

  @Override
  public void delete(Long id)
    throws SQLException, CannotGetJdbcConnectionException {
    String sql = "DELETE FROM questions WHERE id = ?;";
    Connection conn = DataSourceUtils.getConnection(dataSource);

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, id);
      int affectedRows = pstmt.executeUpdate();

      if (affectedRows == 0) {
        throw new SQLException("Failed to delete question");
      }

    } finally {
      releaseConnectionIfNoTransaction(conn);
    }
  }

  @NotNull
  private QuestionModel getQuestionModel(ResultSet rs)
    throws SQLException, CannotGetJdbcConnectionException {
    long id = rs.getLong("id");
    long userId = rs.getLong("user_id");
    String title = rs.getString("title");
    String content = rs.getString("content");
    LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
    long acceptedAnswerId = rs.getLong("accepted_answer_id");

    return new QuestionModel(
      id, userId, title, content, createdAt, acceptedAnswerId
    );
  }
}
