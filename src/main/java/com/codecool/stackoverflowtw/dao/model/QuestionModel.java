package com.codecool.stackoverflowtw.dao.model;

import java.time.LocalDateTime;

public class QuestionModel {

  private final Long id;

  private final Long userId;

  private final String title;

  private final String content;

  private final LocalDateTime createdAt;

  private final Long acceptedAnswerId;

  public QuestionModel(Long id, Long userId, String title, String content, LocalDateTime createdAt, Long acceptedAnswerId) {
    this.id = id;
    this.userId = userId;
    this.title = title;
    this.content = content;
    this.createdAt = createdAt;
    this.acceptedAnswerId = acceptedAnswerId;
  }

  public Long getId() {
    return id;
  }

  public Long getUserId() {
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

  public Long getAcceptedAnswerId() {
    return acceptedAnswerId;
  }
}
