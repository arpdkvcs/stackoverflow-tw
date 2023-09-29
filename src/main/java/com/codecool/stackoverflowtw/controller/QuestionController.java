package com.codecool.stackoverflowtw.controller;

import com.codecool.stackoverflowtw.controller.dto.question.NewQuestionDTO;
import com.codecool.stackoverflowtw.controller.dto.question.QuestionResponseDetailsDTO;
import com.codecool.stackoverflowtw.controller.dto.question.UpdateQuestionDTO;
import com.codecool.stackoverflowtw.controller.dto.user.TokenUserInfoDTO;
import com.codecool.stackoverflowtw.service.question.QuestionService;
import com.codecool.stackoverflowtw.service.user.AccessControlService;
import com.codecool.stackoverflowtw.service.user.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@CrossOrigin(origins = "http://localhost:5000")
@RestController
@RequestMapping("api/questions")
public class QuestionController extends ControllerBase {
  private final QuestionService questionService;

  public QuestionController(TokenService tokenService,
                            AccessControlService accessControlService,
                            QuestionService questionService) {
    super(tokenService, accessControlService);
    this.questionService = questionService;
  }

  @GetMapping("/all")
  public ResponseEntity<?> getAllQuestions() {
    try {
      return handleOkData("Successful request", questionService.getAllQuestions());
    } catch (Exception e) {
      return handleBadRequest("Failed to retrieve questions.", e);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getQuestionById(@PathVariable Long id) {
    try {
      return handleOkData("Successful request", questionService.getQuestionByIdWithAnswers(id));
    } catch (Exception e) {
      return handleBadRequest("Failed to retrieve question.", e);
    }
  }

  @GetMapping("/user/{userid}")
  public ResponseEntity<?> getQuestionByUserId(@PathVariable Long userid, HttpServletRequest request) {
    try {
      return handleOkData("Successful request", questionService.getQuestionsByUserId(userid));
    } catch (Exception e) {
      return handleBadRequest("Failed to get questions of user", e);
    }
  }

  @GetMapping("/search/{searchQuery}")
  public ResponseEntity<?> getQuestionsByTitle(@PathVariable String searchQuery) {
    try {
      return handleOkData("Successful request", questionService.getQuestionsByTitle(searchQuery));
    } catch (Exception e) {
      return handleBadRequest("Failed to perform search.", e);
    }
  }

  @PostMapping("")
  public ResponseEntity<?> addNewQuestion(
    HttpServletRequest request, @RequestBody NewQuestionDTO newQuestionDTO) {
    try {
      Optional<TokenUserInfoDTO> userInfo = verifyToken(request);
      if (userInfo.isPresent()) {
        return handleOkData("Successful request", questionService.addNewQuestion(newQuestionDTO));
      } else {
        return handleUnauthorized("Unauthorized", null);
      }
    } catch (Exception e) {
      return handleBadRequest("Failed to post new question.", e);
    }
  }

  @PatchMapping("")
  public ResponseEntity<?> updateQuestion(
    HttpServletRequest request, @RequestBody UpdateQuestionDTO updateQuestionDTO) {
    try {
      Optional<TokenUserInfoDTO> userInfo = verifyToken(request);
      if (userInfo.isPresent() && userInfo.get().userid() == updateQuestionDTO.userId()) {
        return handleOkData("Successful request",
          questionService.updateQuestion(updateQuestionDTO));
      } else {
        return handleUnauthorized("Unauthorized", null);
      }
    } catch (Exception e) {
      return handleBadRequest("Failed to update question.", e);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteQuestionById(@PathVariable Long id, HttpServletRequest request) {
    try {
      Optional<TokenUserInfoDTO> userInfo = verifyToken(request);
      if (userInfo.isPresent()) {
        QuestionResponseDetailsDTO details = questionService.getQuestionById(id);
        if (true) {
          questionService.deleteQuestionById(id);
          return handleOkMessage("Question has been deleted.");
        }
      }
      return handleUnauthorized("Unauthorized", null);
    } catch (Exception e) {
      return handleBadRequest("Failed to delete question.", e);
    }
  }
}
