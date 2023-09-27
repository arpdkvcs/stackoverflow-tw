package com.codecool.stackoverflowtw.service;

import com.codecool.stackoverflowtw.controller.dto.question.QuestionResponseDTO;
import com.codecool.stackoverflowtw.dao.AnswerDAO;
import com.codecool.stackoverflowtw.dao.QuestionsDAO;
import com.codecool.stackoverflowtw.dao.model.QuestionModel;
import com.codecool.stackoverflowtw.dao.user.UserDAO;
import com.codecool.stackoverflowtw.dao.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
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
        Set<QuestionModel> questionModels = questionsDAO.readAll();
        Set<QuestionResponseDTO> questionResponseDTOs = new HashSet<>();

        for (QuestionModel questionModel : questionModels) {
            long id = questionModel.getId();
            String title = questionModel.getTitle();
            LocalDateTime createdAt = questionModel.getCreatedAt();
            int answerCount = answerDAO.getNumberOfAnswersForQuestion(id);
            String username = userDAO

            questionResponseDTOs.add(new QuestionResponseDTO());
        }

        return null;
    }

    public QuestionDTO getQuestionById(int id) {
        // TODO
        questionsDAO.sayHi();
        return new QuestionDTO(id, "example title", "example desc", LocalDateTime.now());
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
