package com.codecool.stackoverflowtw.dao;

import com.codecool.stackoverflowtw.dao.model.AnswerModel;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnswerDaoJdbc extends BaseDaoJdbc implements AnswerDAO {
    public AnswerDaoJdbc(DataSource dataSource) {
        super(dataSource);
    }

    //create new answer
    @Override
    public void insertAnswer(AnswerModel answer) throws SQLException {
        String sql = "INSERT INTO answers(user_id, question_id, content) VALUES(?,?,?)";

        Connection conn = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setLong(1, answer.getUserId());
            preparedStatement.setLong(2, answer.getQuestionId());
            preparedStatement.setString(3, answer.getContent());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Failed to create new answer");
            }

        } finally {
            releaseConnectionIfNoTransaction(conn);
        }
    }

    //update an answer
    @Override
    public void updateAnswer(AnswerModel answer) throws SQLException {
        String sql = "UPDATE answers SET content=? WHERE id=?";

        Connection conn = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, answer.getContent());
            preparedStatement.setLong(2, answer.getId());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Failed to update answer");
            }
        } finally {
            releaseConnectionIfNoTransaction(conn);
        }
    }

    //delete an answer
    @Override
    public void deleteAnswer(long id) throws SQLException {
        String sql = "DELETE FROM answers WHERE id=?";

        Connection conn = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Failed to delete answer");
            }
        } finally {
            releaseConnectionIfNoTransaction(conn);
        }
    }


    //fetch answers for a specific question
    @Override
    public List<AnswerModel> getAnswerByQuestionId(long questionId) throws SQLException {
        List<AnswerModel> answers = new ArrayList<>();
        String sql = "SELECT * FROM answers WHERE question_id=?";

        Connection conn = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setLong(1, questionId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                answers.add(new AnswerModel(
                        resultSet.getLong("id"),
                        resultSet.getLong("user_id"),
                        resultSet.getLong("question_id"),
                        resultSet.getString("content"),
                        resultSet.getTimestamp("created_at").toLocalDateTime()));
            }
        } finally {
            releaseConnectionIfNoTransaction(conn);
        }
        return answers;
    }


}
