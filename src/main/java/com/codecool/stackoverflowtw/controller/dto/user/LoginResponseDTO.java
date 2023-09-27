package com.codecool.stackoverflowtw.controller.dto.user;

import com.codecool.stackoverflowtw.dao.user.model.Role;

import java.util.Set;

public record LoginResponseDTO(long userid, String username, Set<Role> roles, String sessionToken) {
}
