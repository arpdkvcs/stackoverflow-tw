package com.codecool.stackoverflowtw.dao.question;

import com.codecool.stackoverflowtw.dao.model.QuestionModel;

import java.sql.SQLException;
import java.util.Set;

public interface QuestionsDAO {

    void create(QuestionModel questionModel) throws SQLException;

    Set<QuestionModel> readAll() throws SQLException;

    QuestionModel readById(long questionId) throws SQLException;

    Set<QuestionModel> readByTitle(String searchQuery) throws SQLException;

    void update(QuestionModel questionModel) throws SQLException;

    void delete(long id) throws SQLException;
}
