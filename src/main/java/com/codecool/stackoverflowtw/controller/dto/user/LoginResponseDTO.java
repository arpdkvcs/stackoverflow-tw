package com.codecool.stackoverflowtw.controller.dto.user;

import com.codecool.stackoverflowtw.dao.model.Role;

import java.util.Set;

public record LoginResponseDTO(Long userid, String username, Set<Role> roles, String sessionToken) {
}
