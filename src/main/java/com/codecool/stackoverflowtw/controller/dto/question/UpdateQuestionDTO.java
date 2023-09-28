package com.codecool.stackoverflowtw.controller.dto.question;

public record UpdateQuestionDTO(Long id,
                                Long userId,
                                String title,
                                String content,
                                Long acceptedAnswerId) {
}
