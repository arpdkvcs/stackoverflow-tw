package com.codecool.stackoverflowtw.controller.dto.answer;

import java.time.LocalDateTime;

public record AnswerResponseDetailsDTO(long id, String content, LocalDateTime createdAt, String username) {
}
