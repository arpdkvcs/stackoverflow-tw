package com.codecool.stackoverflowtw.controller.dto.question;

import com.codecool.stackoverflowtw.controller.dto.answer.AnswerResponseDetailsDTO;

import java.time.LocalDateTime;
import java.util.Set;

public record QuestionResponseDetailsWithAnswersDTO(Long id,
                                                    String title,
                                                    String content,
                                                    LocalDateTime createdAt,
                                                    Set<AnswerResponseDetailsDTO> answers,
                                                    String username) {
}
