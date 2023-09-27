package com.codecool.stackoverflowtw.service;

import com.codecool.stackoverflowtw.controller.dto.question.QuestionResponseDTO;
import com.codecool.stackoverflowtw.controller.dto.question.QuestionResponseDetailsDTO;
import com.codecool.stackoverflowtw.dao.AnswerDAO;
import com.codecool.stackoverflowtw.dao.QuestionsDAO;
import com.codecool.stackoverflowtw.dao.model.QuestionModel;
import com.codecool.stackoverflowtw.dao.user.UserDAO;
import com.codecool.stackoverflowtw.dao.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class QuestionService {


    private final QuestionsDAO questionsDAO;

    private final AnswerDAO answerDAO;

    private  final UserDAO userDAO;
    @Autowired
    public QuestionService(QuestionsDAO questionsDAO, AnswerDAO answerDAO, UserDAO userDAO) {
        this.questionsDAO = questionsDAO;
        this.answerDAO = answerDAO;
        this.userDAO = userDAO;
    }

    public Set<QuestionResponseDTO> getAllQuestions() throws SQLException {

        try {
            Set<QuestionModel> questionModels = questionsDAO.readAll();
            Set<QuestionResponseDTO> questionResponseDTOs = new HashSet<>();

            for (QuestionModel questionModel : questionModels) {
                long id = questionModel.getId();
                String title = questionModel.getTitle();
                LocalDateTime createdAt = questionModel.getCreatedAt();
                int answerCount = answerDAO.getNumberOfAnswersForQuestion(id);
                String username = userDAO.getUsernameById(questionModel.getUserId());

                questionResponseDTOs.add(new QuestionResponseDTO(
                        id, title, createdAt, answerCount, username));
            }
            return questionResponseDTOs;

        } catch (CannotGetJdbcConnectionException e) {
            throw new SQLException(e);
        }

    }

    public QuestionResponseDetailsDTO getQuestionById(int questionId) throws SQLException {
        try {
            QuestionModel questionModel = questionsDAO.readById(questionId);
            long id = questionModel.getId();
            String title = questionModel.getTitle();
            String content = questionModel.getContent();
            LocalDateTime createdAt = questionModel.getCreatedAt();
            Set<Long> answersIds = answerDAO.

            return new QuestionResponseDetailsDTO();
        } catch (CannotGetJdbcConnectionException e) {
            throw new SQLException(e);
        }
        return null;
    }

    public boolean deleteQuestionById(int id) {
        // TODO
        return false;
    }

    public int addNewQuestion(NewQuestionDTO question) {
        // TODO
        int createdId = 0;
        return createdId;
    }
}
