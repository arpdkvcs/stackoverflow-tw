package com.codecool.stackoverflowtw.service.answer;

import com.codecool.stackoverflowtw.controller.dto.answer.NewAnswerDTO;
import com.codecool.stackoverflowtw.controller.dto.answer.UpdateAnswerDTO;

import java.sql.SQLException;

public interface AnswerService {
    void createAnswer(NewAnswerDTO newAnswerDTO) throws SQLException;
    void updateAnswer(UpdateAnswerDTO updateAnswerDTO) throws SQLException;
    void deleteAnswer(long id) throws SQLException;

}
