package com.codecool.stackoverflowtw.controller.dto.question;

import java.time.LocalDateTime;
import java.util.Set;

public record QuestionResponseDetailsDTO(long id, String title, String content,
                                         LocalDateTime createdAt,
                                         Set<Long> answersIds, String username) {
}
