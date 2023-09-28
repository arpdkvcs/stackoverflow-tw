package com.codecool.stackoverflowtw.dao;

import com.codecool.stackoverflowtw.dao.model.QuestionModel;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
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
    public void create(QuestionModel questionModel)
            throws SQLException, CannotGetJdbcConnectionException {
        String sql = "INSERT INTO questions (user_id, title, content) VALUES (?, ?, ?);";
        Connection conn = DataSourceUtils.getConnection(dataSource);

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, questionModel.getUserId());
            pstmt.setString(2, questionModel.getTitle());
            pstmt.setString(3, questionModel.getContent());

            int affectedRow = pstmt.executeUpdate();

            if (affectedRow == 0) {
                throw new SQLException("No record has been created in questions table.");
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
    public QuestionModel readById(long questionId)
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
                        "content = ?, " +
                        "accepted_answer_id = ? " +
                        "WHERE id = ?;";
        Connection conn = DataSourceUtils.getConnection(dataSource);

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, questionModel.getTitle());
            pstmt.setString(2, questionModel.getContent());
            pstmt.setLong(3, questionModel.getAcceptedAnswerId());
            pstmt.setLong(4, questionModel.getId());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Couldn't update question in questions table.");
            }

        } finally {
            releaseConnectionIfNoTransaction(conn);
        }
    }

    @Override
    public void delete(long id)
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
}
