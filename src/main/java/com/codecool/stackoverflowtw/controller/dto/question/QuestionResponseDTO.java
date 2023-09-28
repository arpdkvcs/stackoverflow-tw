package com.codecool.stackoverflowtw.controller.dto.question;

import java.time.LocalDateTime;

public record QuestionResponseDTO(Long id, String title, LocalDateTime createdAt,
                                  int answerCount, String username) {
}
