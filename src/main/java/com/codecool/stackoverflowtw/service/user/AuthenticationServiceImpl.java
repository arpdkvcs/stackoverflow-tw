package com.codecool.stackoverflowtw.service.user;

import com.codecool.stackoverflowtw.controller.dto.user.*;
import com.codecool.stackoverflowtw.dao.model.Role;
import com.codecool.stackoverflowtw.dao.model.UserModel;
import com.codecool.stackoverflowtw.dao.user.UserDAO;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
  private final TokenService tokenService;
  private final AccessControlService accessControlService;
  private final UserDAO userDAO;

  @Autowired
  public AuthenticationServiceImpl(TokenService tokenService, AccessControlService accessControlService, UserDAO userDAO) {
    this.tokenService = tokenService;
    this.accessControlService = accessControlService;
    this.userDAO = userDAO;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void register(NewUserDTO user) throws Exception {
    userDAO.create(user.username(), BCrypt.hashpw(user.password(), BCrypt.gensalt(10)));
    Optional<UserModel> foundUser = userDAO.readByUsername(user.username());
    if (foundUser.isPresent()) {
      accessControlService.assignToUser(foundUser.get().getId(), Role.USER);
    } else {
      throw new RuntimeException("Failed to create new user " + user.username());
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public LoginResponseDTO login(LoginUserDTO user) throws Exception {
    Optional<UserModel> userToFind = userDAO.readByUsername(user.username());
    if (userToFind.isEmpty()) {
      throw new RuntimeException("Username or password doesn't match");
    }

    UserModel foundUser = userToFind.get();
    if (!BCrypt.checkpw(user.password(), foundUser.getHashedPassword())) {
      throw new RuntimeException("Username or password doesn't match");
    }

    //Set<Role> roles = accessControlService.readAllOfUser(foundUser.getId());
    if (!foundUser.getRoles().contains(Role.USER)) {
      throw new RuntimeException("UserModel account with username " + user.username() +
        " is deactivated");
    }

    String sessionToken = tokenService.sign(new TokenUserInfoDTO(foundUser.getId(),
      foundUser.getUsername(),
      foundUser.getRoles()));
    tokenService.addToUser(foundUser.getId(), sessionToken);


    return new LoginResponseDTO(foundUser.getId(), foundUser.getUsername(), foundUser.getRoles(),
      sessionToken);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void logout(LogoutUserDTO logoutUserDTO) throws Exception {
    try {
      tokenService.removeFromUser(logoutUserDTO.userid(), logoutUserDTO.sessionToken());
    } catch (Exception e) {
      tokenService.removeAllFromUser(logoutUserDTO.userid());
      throw new RuntimeException("Error during logout at user with ID " + logoutUserDTO.userid() +
        ", clearing all sessions");
    }
  }
}
