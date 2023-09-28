package com.codecool.stackoverflowtw.controller.dto.answer;

import java.time.LocalDateTime;

public record AnswerResponseDetailsDTO(Long id, String content, LocalDateTime createdAt, String username) {
}
