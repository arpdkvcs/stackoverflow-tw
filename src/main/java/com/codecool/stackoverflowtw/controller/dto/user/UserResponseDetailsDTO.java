package com.codecool.stackoverflowtw.controller.dto.user;

import com.codecool.stackoverflowtw.dao.model.Role;

import java.util.Set;

public record UserResponseDetailsDTO(Long id, String username, Set<Role> roles) {
}
