package com.codecool.stackoverflowtw.service.answer;

import com.codecool.stackoverflowtw.controller.dto.answer.AnswerResponseDetailsDTO;
import com.codecool.stackoverflowtw.controller.dto.answer.NewAnswerDTO;
import com.codecool.stackoverflowtw.controller.dto.answer.UpdateAnswerDTO;
import com.codecool.stackoverflowtw.dao.AnswerDAO;
import com.codecool.stackoverflowtw.dao.model.AnswerModel;
import com.codecool.stackoverflowtw.dao.user.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {

    private final UserDAO userDAO;
    private final AnswerDAO answerDAO;

    @Autowired
    public AnswerServiceImpl(UserDAO userDAO, AnswerDAO answerDAO) {
        this.userDAO = userDAO;
        this.answerDAO = answerDAO;
    }

    @Override
    public void createAnswer(NewAnswerDTO newAnswerDTO) throws SQLException {
        try {
            AnswerModel answer = new AnswerModel(0, newAnswerDTO.userId(), 0, newAnswerDTO.content(), LocalDateTime.now());
            answerDAO.insertAnswer(answer);
        } catch (CannotGetJdbcConnectionException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateAnswer(UpdateAnswerDTO updateAnswerDTO) throws SQLException {
        try {
            answerDAO.updateAnswer(
                    new AnswerModel(updateAnswerDTO.id(), -1, -1, updateAnswerDTO.content(), null));
        } catch (CannotGetJdbcConnectionException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void deleteAnswer(long id) throws SQLException {
        try {
            answerDAO.deleteAnswer(id);
        } catch (CannotGetJdbcConnectionException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<AnswerResponseDetailsDTO> getAnswerByQuestionId(long questionId) throws SQLException {
        List<AnswerModel> answerModels = answerDAO.getAnswerByQuestionId((questionId));
        List<AnswerResponseDetailsDTO> responseList = new ArrayList<>();

        for (AnswerModel answer : answerModels) {
            String username = String.valueOf(userDAO.readById(answer.getUserId()));
            AnswerResponseDetailsDTO responseDTO = new AnswerResponseDetailsDTO(
                    answer.getId(),
                    answer.getContent(),
                    answer.getCreatedAt(),
                    username
            );
            responseList.add(responseDTO);
        }
        return responseList;
    }


}
