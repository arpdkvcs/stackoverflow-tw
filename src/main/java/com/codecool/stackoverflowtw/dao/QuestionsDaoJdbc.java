package com.codecool.stackoverflowtw.dao;

import com.codecool.stackoverflowtw.dao.model.QuestionModel;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    public Set<QuestionModel> readAll() {
        return null;
    }

    @Override
    public QuestionModel readById(long questionId) {
        return null;
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
