package com.codecool.stackoverflowtw.dao;

import com.codecool.stackoverflowtw.dao.model.QuestionModel;

import java.sql.SQLException;
import java.util.Set;

public interface QuestionsDAO {

    void create(QuestionModel questionModel) throws SQLException;

    Set<QuestionModel> readAll();

    QuestionModel readById(long questionId);

    Set<QuestionModel> readByTitle(String searchQuery);

    void update(QuestionModel questionModel);

    void delete(long id);
}
