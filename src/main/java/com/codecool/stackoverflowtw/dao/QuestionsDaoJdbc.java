package com.codecool.stackoverflowtw.dao;

import com.codecool.stackoverflowtw.dao.model.QuestionModel;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class QuestionsDaoJdbc extends BaseDaoJdbc implements QuestionsDAO {
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
        String sql = "SELECT * FROM questions";
        Connection conn = DataSourceUtils.getConnection(dataSource);

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

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
        String sql = String.format("SELECT * FROM questions WHERE id = %s", questionId);
        Connection conn = DataSourceUtils.getConnection(dataSource);

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return getQuestionModel(rs);
            }

        } finally {
            releaseConnectionIfNoTransaction(conn);
        }
        return null;
    }

    @NotNull
    private QuestionModel getQuestionModel(ResultSet rs) throws SQLException {
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
    public Set<QuestionModel> readByTitle(String searchQuery) {
        return null;
    }

    @Override
    public void update(QuestionModel questionModel) {

    }

    @Override
    public void delete(long id) {

    }
}
