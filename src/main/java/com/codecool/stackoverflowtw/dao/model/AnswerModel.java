package com.codecool.stackoverflowtw.dao.model;

import java.time.LocalDateTime;

public class AnswerModel {
    private final long id;
    private final long userId;
    private final long questionId;
    private final String content;
    private final LocalDateTime createdAt;

    public AnswerModel(long id, long userId, long questionId, String content, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.questionId = questionId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public long getQuestionId() {
        return questionId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


}
