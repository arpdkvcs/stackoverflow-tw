package com.codecool.stackoverflowtw.controller.dto.question;

public record UpdateQuestionDTO(long id,
                                long userId,
                                String title,
                                String content,
                                long acceptedAnswerId) {
}
