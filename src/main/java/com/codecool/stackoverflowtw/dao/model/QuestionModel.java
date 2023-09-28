package com.codecool.stackoverflowtw.dao.model;

import java.time.LocalDateTime;

public class QuestionModel {

    private final long id;

    private final long userId;

    private final String title;

    private final String content;

    private final LocalDateTime createdAt;

    private final long acceptedAnswerId;

    public QuestionModel(long id, long userId, String title, String content, LocalDateTime createdAt, long acceptedAnswerId) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.acceptedAnswerId = acceptedAnswerId;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public long getAcceptedAnswerId() {
        return acceptedAnswerId;
    }
}
