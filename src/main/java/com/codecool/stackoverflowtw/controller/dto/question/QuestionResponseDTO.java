package com.codecool.stackoverflowtw.controller.dto.question;

import java.time.LocalDateTime;

public record QuestionResponseDTO(long id, String title, String content, LocalDateTime createdAt,
                                  int answerCount, String username) {
}
