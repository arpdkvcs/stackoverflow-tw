package com.codecool.stackoverflowtw.dao.question;

import com.codecool.stackoverflowtw.dao.model.QuestionModel;

import java.sql.SQLException;
import java.util.Set;

public interface QuestionsDAO {

  int create(QuestionModel questionModel) throws SQLException;

  Set<QuestionModel> readAll() throws SQLException;

  QuestionModel readById(Long questionId) throws SQLException;

  Set<QuestionModel> readByTitle(String searchQuery) throws SQLException;

  void update(QuestionModel questionModel) throws SQLException;

  void delete(Long id) throws SQLException;
}
