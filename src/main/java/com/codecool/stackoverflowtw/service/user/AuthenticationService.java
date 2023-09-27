package com.codecool.stackoverflowtw.service.user;

import com.codecool.stackoverflowtw.controller.dto.user.LoginResponseDTO;
import com.codecool.stackoverflowtw.controller.dto.user.NewUserDTO;
import com.codecool.stackoverflowtw.controller.dto.user.LoginUserDTO;

public interface AuthenticationService {
  void register(NewUserDTO user) throws Exception;

  LoginResponseDTO login(LoginUserDTO user) throws Exception;

  void logout(long userid, String sessionToken) throws Exception;
}
