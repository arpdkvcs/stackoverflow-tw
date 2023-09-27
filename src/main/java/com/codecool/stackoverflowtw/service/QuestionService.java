package com.codecool.stackoverflowtw.service;

import com.codecool.stackoverflowtw.controller.dto.question.QuestionResponseDTO;
import com.codecool.stackoverflowtw.dao.AnswerDAO;
import com.codecool.stackoverflowtw.dao.QuestionsDAO;
import com.codecool.stackoverflowtw.dao.model.QuestionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class QuestionService {


    private final QuestionsDAO questionsDAO;

    private final AnswerDAO answerDAO;
    @Autowired
    public QuestionService(QuestionsDAO questionsDAO, AnswerDAO answerDAO) {
        this.questionsDAO = questionsDAO;
        this.answerDAO = answerDAO;
    }

    public Set<QuestionResponseDTO> getAllQuestions() throws SQLException {
        Set<QuestionModel> questionModels = questionsDAO.readAll();
        Set<QuestionResponseDTO> questionResponseDTOs = new HashSet<>();

        for (QuestionModel questionModel : questionModels) {

        }

        return List.of(new QuestionResponseDTO(1, "example title", "example desc", LocalDateTime.now()));
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
