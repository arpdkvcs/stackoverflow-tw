package com.codecool.stackoverflowtw.service.answer;

import com.codecool.stackoverflowtw.controller.dto.answer.AnswerResponseDetailsDTO;
import com.codecool.stackoverflowtw.controller.dto.answer.NewAnswerDTO;
import com.codecool.stackoverflowtw.controller.dto.answer.UpdateAnswerDTO;

import java.sql.SQLException;
import java.util.List;

public interface AnswerService {
  void createAnswer(NewAnswerDTO newAnswerDTO) throws SQLException;

  void updateAnswer(UpdateAnswerDTO updateAnswerDTO) throws SQLException;

  void deleteAnswer(long id) throws SQLException;

  List<AnswerResponseDetailsDTO> getAnswerByQuestionId(long questionId) throws SQLException;

}
