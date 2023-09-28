package com.codecool.stackoverflowtw.dao.model;

import java.time.LocalDateTime;

public class AnswerModel {
  private final Long id;
  private final Long userId;
  private final Long questionId;
  private final String content;
  private final LocalDateTime createdAt;

  public AnswerModel(Long id, Long userId, Long questionId, String content, LocalDateTime createdAt) {
    this.id = id;
    this.userId = userId;
    this.questionId = questionId;
    this.content = content;
    this.createdAt = createdAt;
  }

  public Long getId() {
    return id;
  }

  public Long getUserId() {
    return userId;
  }

  public Long getQuestionId() {
    return questionId;
  }

  public String getContent() {
    return content;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }


}
