package com.codecool.stackoverflowtw.service.user;

import com.codecool.stackoverflowtw.controller.dto.user.UpdateUserDTO;
import com.codecool.stackoverflowtw.controller.dto.user.UserResponseDTO;
import com.codecool.stackoverflowtw.controller.dto.user.UserResponseDetailsDTO;
import com.codecool.stackoverflowtw.dao.model.UserModel;
import com.codecool.stackoverflowtw.dao.user.UserDAO;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
  private final UserDAO userDAO;

  @Autowired
  public UserServiceImpl(UserDAO userDAO) {
    this.userDAO = userDAO;
  }


  private Optional<UserResponseDetailsDTO> getUserResponseDetailsDTO(Optional<UserModel> userModel) {
    return userModel.map(user -> new UserResponseDetailsDTO(user.getId(),
      user.getUsername(), user.getRoles()));
  }

  @Override
  public Set<UserResponseDTO> readAll() throws SQLException {
    try {
      Set<UserModel> userModels = userDAO.readAll();
      return userModels.stream().map(u -> new UserResponseDTO(u.getId(), u.getUsername()))
        .collect(Collectors.toSet());
    } catch (CannotGetJdbcConnectionException e) {
      throw new SQLException(e);
    }
  }

  @Override
  public Optional<UserResponseDetailsDTO> readByUsername(String username) throws SQLException {
    try {
      Optional<UserModel> userModel = userDAO.readByUsername(username);
      return getUserResponseDetailsDTO(userModel);
    } catch (CannotGetJdbcConnectionException e) {
      throw new SQLException(e);
    }
  }

  @Override
  public Optional<UserResponseDetailsDTO> readById(Long id) throws SQLException {
    try {
      Optional<UserModel> userModel = userDAO.readById(id);
      return getUserResponseDetailsDTO(userModel);
    } catch (CannotGetJdbcConnectionException e) {
      throw new SQLException(e);
    }
  }

  @Override
  public void update(UpdateUserDTO user) throws SQLException {
    try {
      userDAO.update(user.id(), user.username(),
        BCrypt.hashpw(user.rawPassword(), BCrypt.gensalt(10)));
    } catch (CannotGetJdbcConnectionException e) {
      throw new SQLException(e);
    }

  }

  @Override
  public void delete(Long id) throws SQLException {
    try {
      userDAO.delete(id);
    } catch (CannotGetJdbcConnectionException e) {
      throw new SQLException(e);
    }

  }
}
