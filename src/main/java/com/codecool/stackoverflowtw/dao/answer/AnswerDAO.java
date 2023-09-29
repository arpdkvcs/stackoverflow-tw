package com.codecool.stackoverflowtw.dao.answer;

import com.codecool.stackoverflowtw.dao.model.AnswerModel;
import org.springframework.jdbc.CannotGetJdbcConnectionException;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface AnswerDAO {

  //create new answer
  void insertAnswer(AnswerModel model) throws SQLException;

  //update an answer
  void updateAnswer(AnswerModel answer) throws SQLException;

  //delete an answer
  void deleteAnswer(Long id) throws SQLException;


  //fetch answers for a specific question
  Set<AnswerModel> getAnswersByQuestionId(Long questionId) throws SQLException;

  int getNumberOfAnswersForQuestion(Long questionId) throws SQLException;

  Set<Long> getAnswersIdsForQuestion(Long questionId) throws SQLException;

  List<AnswerModel> getAnswerByQuestionId(Long questionId);
  AnswerModel getAnswerById(Long answerId) throws SQLException,
  CannotGetJdbcConnectionException;
}
