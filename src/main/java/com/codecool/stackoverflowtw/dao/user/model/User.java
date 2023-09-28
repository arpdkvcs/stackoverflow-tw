package com.codecool.stackoverflowtw.dao.user.model;

import java.util.Set;

public class User {
  private final Long id;
  private String username;
  private String hashedPassword;
  private final Set<Role> roles;
  private final Set<String> hashedSessionTokens;

  public User(Long id, String username, String hashedPassword, Set<Role> roles, Set<String> hashedSessionTokens) {
    this.id = id;
    this.username = username;
    this.hashedPassword = hashedPassword;
    this.roles = roles;
    this.hashedSessionTokens = hashedSessionTokens;
  }

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getHashedPassword() {
    return hashedPassword;
  }

  public void setHashedPassword(String hashedPassword) {
    this.hashedPassword = hashedPassword;
  }

  public Set<Role> getRoles() {
    return Set.copyOf(roles);
  }

  public void addRole(Role role) {
    this.roles.add(role);
  }

  public void removeRole(Role role) {
    this.roles.remove(role);
  }

  public Set<String> getHashedSessionTokens() {
    return Set.copyOf(hashedSessionTokens);
  }

  public void addHashedSessionToken(String hashedSessionToken) {
    this.hashedSessionTokens.add(hashedSessionToken);
  }

  public void removeHashedSessionToken(String hashedSessionToken) {
    this.hashedSessionTokens.remove(hashedSessionToken);
  }
}
