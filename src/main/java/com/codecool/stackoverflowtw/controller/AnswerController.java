package com.codecool.stackoverflowtw.controller;

import com.codecool.stackoverflowtw.controller.dto.answer.AnswerResponseDetailsDTO;
import com.codecool.stackoverflowtw.controller.dto.answer.NewAnswerDTO;
import com.codecool.stackoverflowtw.controller.dto.answer.UpdateAnswerDTO;
import com.codecool.stackoverflowtw.controller.dto.user.TokenUserInfoDTO;
import com.codecool.stackoverflowtw.service.answer.AnswerService;
import com.codecool.stackoverflowtw.service.user.AccessControlService;
import com.codecool.stackoverflowtw.service.user.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5000")
@RestController
@RequestMapping("api/answers")
public class AnswerController extends ControllerBase {
  private final AnswerService answerService;

  public AnswerController(TokenService tokenService,
                          AccessControlService accessControlService,
                          AnswerService answerService) {
    super(tokenService, accessControlService);
    this.answerService = answerService;
  }

  //Create an answer
  @PostMapping("")
  public ResponseEntity<?> createAnswer(
    @RequestBody NewAnswerDTO newAnswerDTO, HttpServletRequest request) {
    try {
      Optional<TokenUserInfoDTO> userInfo = verifyToken(request);
      if (userInfo.isPresent()) {
        answerService.createAnswer(newAnswerDTO);
        return handleOkMessage("Answer created successfully");
      } else {
        return handleUnauthorized("Unauthorized", null);
      }
    } catch (Exception e) {
      return handleBadRequest("Failed to create answer", e);
    }
  }

  //update an answer
  @PatchMapping("")
  public ResponseEntity<?> updateAnswer(
    @RequestBody UpdateAnswerDTO updateAnswerDTO, HttpServletRequest request) {
    try {
      Optional<TokenUserInfoDTO> userInfo = verifyToken(request);
      if (userInfo.isPresent() && userInfo.get().userid() == updateAnswerDTO.userId()) {
        answerService.updateAnswer(updateAnswerDTO);
        return handleOkMessage("Answer updated successfully");
      } else {
        return handleUnauthorized("Unauthorized", null);
      }
    } catch (Exception e) {
      return handleBadRequest("Failed to update answer", e);
    }
  }

  //delete an answer
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteAnswer(
    @PathVariable Long id, HttpServletRequest request) {
    try {
      Optional<TokenUserInfoDTO> userInfo = verifyToken(request);
      if (userInfo.isPresent()) {
        if (true/*details.username().equals(userInfo.get().username())*/) {
          answerService.deleteAnswer(id);
          return handleOkMessage("Answer deleted successfully");
        }
      }
      return handleUnauthorized("Unauthorized", null);
    } catch (Exception e) {
      return handleBadRequest("Failed to delete answer", e);
    }
  }

  //Get one answer by id.. sry don't want to modify the other one but that should be renamed...
  @GetMapping("/id/{answerId}")
  public ResponseEntity<?> getAnswerById(@PathVariable Long answerId) {
    try {
      return handleOkData("Fetched answers for question", answerService.getAnswerById(answerId));
    } catch (Exception e) {
      return handleBadRequest("Failed to fetch answers for this question", e);
    }
  }

  //Get all answers for a specific question
  @GetMapping("/{questionId}")
  public ResponseEntity<?> getAnswerByQuestionId(@PathVariable Long questionId) {
    try {
      return handleOkData("Fetched answers for question", answerService.getAnswerByQuestionId(questionId));
    } catch (Exception e) {
      return handleBadRequest("Failed to fetch answers for this question", e);
    }
  }


}
