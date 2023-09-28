package com.codecool.stackoverflowtw.dao.answer;

import com.codecool.stackoverflowtw.dao.model.AnswerModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface AnswerDAO {

    //create new answer
    void insertAnswer(AnswerModel model) throws SQLException;

    //update an answer
    void updateAnswer(AnswerModel answer) throws SQLException;

    //delete an answer
    void deleteAnswer(long id) throws SQLException;


    //fetch answers for a specific question
    Set<AnswerModel> getAnswersByQuestionId(long questionId) throws SQLException;

    int getNumberOfAnswersForQuestion(long questionId) throws SQLException;

    Set<Long> getAnswersIdsForQuestion(long questionId) throws SQLException;

  List<AnswerModel> getAnswerByQuestionId(long questionId);
}
