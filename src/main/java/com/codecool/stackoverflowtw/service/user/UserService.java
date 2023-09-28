package com.codecool.stackoverflowtw.service.user;

import com.codecool.stackoverflowtw.controller.dto.user.UpdateUserDTO;
import com.codecool.stackoverflowtw.controller.dto.user.UserResponseDTO;
import com.codecool.stackoverflowtw.controller.dto.user.UserResponseDetailsDTO;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

public interface UserService {
  Set<UserResponseDTO> readAll() throws SQLException;

  Optional<UserResponseDetailsDTO> readByUsername(String username) throws SQLException;

  Optional<UserResponseDetailsDTO> readById(Long id) throws SQLException;

  void update(UpdateUserDTO user) throws SQLException;

  void delete(Long id) throws SQLException;
}
