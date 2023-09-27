package com.codecool.stackoverflowtw.controller.dto.user;

import com.codecool.stackoverflowtw.dao.user.model.Role;

import java.util.Set;

public record UserResponseDetailsDTO(long id, String username, Set<Role> roles) {
}