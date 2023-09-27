package com.codecool.stackoverflowtw.dao;

import com.codecool.stackoverflowtw.dao.model.AnswerModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AnswerDAO {

    //create new answer
    void insertAnswer(AnswerModel model) throws SQLException;

    //update an answer
    void updateAnswer(AnswerModel answer) throws SQLException;

    //delete an answer
    void deleteAnswer(long id) throws SQLException;


    //fetch answers for a specific question
    List<AnswerModel> getAnswerByQuestionId(long questionId) throws SQLException;

}
