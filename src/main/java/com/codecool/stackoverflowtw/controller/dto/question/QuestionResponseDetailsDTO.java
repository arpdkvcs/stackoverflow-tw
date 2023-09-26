package com.codecool.stackoverflowtw.controller.dto.question;

import com.codecool.stackoverflowtw.controller.dto.answer.AnswerResponseDetailsDTO;

import java.time.LocalDateTime;
import java.util.List;

public record QuestionResponseDetailsDTO(long id, String title, String content,
                                         LocalDateTime createdAt,
                                         List<AnswerResponseDetailsDTO> answers, String username) {
}
