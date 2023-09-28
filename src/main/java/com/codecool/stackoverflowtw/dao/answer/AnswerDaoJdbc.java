package com.codecool.stackoverflowtw.dao.answer;

import com.codecool.stackoverflowtw.dao.BaseDaoJdbc;
import com.codecool.stackoverflowtw.dao.model.AnswerModel;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class AnswerDaoJdbc extends BaseDaoJdbc implements AnswerDAO {
  public AnswerDaoJdbc(DataSource dataSource) {
    super(dataSource);
  }

  private static void checkAffectedRowsNotZero(
    int affectedRows, String Failed_to_create_new_answer) throws SQLException {
    if (affectedRows == 0) {
      throw new SQLException(Failed_to_create_new_answer);
    }
  }

  //create new answer
  @Override
  public void insertAnswer(AnswerModel answer)
    throws SQLException, CannotGetJdbcConnectionException {
    String sql = "INSERT INTO answers(user_id, question_id, content) VALUES(?,?,?)";

    Connection conn = DataSourceUtils.getConnection(dataSource);
    try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

      preparedStatement.setLong(1, answer.getUserId());
      preparedStatement.setLong(2, answer.getQuestionId());
      preparedStatement.setString(3, answer.getContent());
      int affectedRows = preparedStatement.executeUpdate();

      checkAffectedRowsNotZero(affectedRows, "Failed to create new answer");

    } finally {
      releaseConnectionIfNoTransaction(conn);
    }
  }

  //update an answer
  @Override
  public void updateAnswer(AnswerModel answer)
    throws SQLException, CannotGetJdbcConnectionException {
    String sql = "UPDATE answers SET content=? WHERE id=?";

    Connection conn = DataSourceUtils.getConnection(dataSource);
    try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

      preparedStatement.setString(1, answer.getContent());
      preparedStatement.setLong(2, answer.getId());
      int affectedRows = preparedStatement.executeUpdate();

      checkAffectedRowsNotZero(affectedRows, "Failed to update answer");
    } finally {
      releaseConnectionIfNoTransaction(conn);
    }
  }

  //delete an answer
  @Override
  public void deleteAnswer(Long id)
    throws SQLException, CannotGetJdbcConnectionException {
    String sql = "DELETE FROM answers WHERE id=?";

    Connection conn = DataSourceUtils.getConnection(dataSource);
    try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

      preparedStatement.setLong(1, id);
      int affectedRows = preparedStatement.executeUpdate();

      checkAffectedRowsNotZero(affectedRows, "Failed to delete answer");
    } finally {
      releaseConnectionIfNoTransaction(conn);
    }
  }


  //fetch answers for a specific question
  @Override
  public Set<AnswerModel> getAnswersByQuestionId(Long questionId)
    throws SQLException, CannotGetJdbcConnectionException {
    Set<AnswerModel> answers = new HashSet<>();
    String sql = "SELECT * FROM answers WHERE question_id=?";

    Connection conn = DataSourceUtils.getConnection(dataSource);
    try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

      preparedStatement.setLong(1, questionId);

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          answers.add(new AnswerModel(
            resultSet.getLong("id"),
            resultSet.getLong("user_id"),
            resultSet.getLong("question_id"),
            resultSet.getString("content"),
            resultSet.getTimestamp("created_at").toLocalDateTime()));
        }
      }
    } finally {
      releaseConnectionIfNoTransaction(conn);
    }
    return answers;
  }

  @Override
  public int getNumberOfAnswersForQuestion(Long questionId)
    throws SQLException, CannotGetJdbcConnectionException {
    String sql = "SELECT COUNT(id) FROM answers WHERE question_id = ?;";
    Connection conn = DataSourceUtils.getConnection(dataSource);

    try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
      preparedStatement.setLong(1, questionId);

      ResultSet rs = preparedStatement.executeQuery();

      if (rs.next()) {
        return rs.getInt("COUNT");
      }

    } finally {
      releaseConnectionIfNoTransaction(conn);
    }
    return 0;
  }

  @Override
  public Set<Long> getAnswersIdsForQuestion(Long questionId)
    throws SQLException, CannotGetJdbcConnectionException {
    Set<Long> answers = new HashSet<>();
    String sql = "SELECT id FROM answers WHERE question_id=?";

    Connection conn = DataSourceUtils.getConnection(dataSource);
    try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

      preparedStatement.setLong(1, questionId);
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        answers.add(resultSet.getLong("id"));
      }
    } finally {
      releaseConnectionIfNoTransaction(conn);
    }
    return answers;
  }

  @Override
  public List<AnswerModel> getAnswerByQuestionId(Long questionId) {
    //TODO: impl
    return null;
  }
}
