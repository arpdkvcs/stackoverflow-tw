package com.codecool.stackoverflowtw.service.user;

import com.codecool.stackoverflowtw.controller.dto.user.LoginResponseDTO;
import com.codecool.stackoverflowtw.controller.dto.user.LoginUserDTO;
import com.codecool.stackoverflowtw.controller.dto.user.LogoutUserDTO;
import com.codecool.stackoverflowtw.controller.dto.user.NewUserDTO;

public interface AuthenticationService {
  void register(NewUserDTO user) throws Exception;

  LoginResponseDTO login(LoginUserDTO user) throws Exception;

  void logout(LogoutUserDTO logoutUserDTO) throws Exception;
}
