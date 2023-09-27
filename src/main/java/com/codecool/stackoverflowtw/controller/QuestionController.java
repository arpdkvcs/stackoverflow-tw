package com.codecool.stackoverflowtw.controller;

import com.codecool.stackoverflowtw.controller.dto.question.NewQuestionDTO;
import com.codecool.stackoverflowtw.controller.dto.user.TokenUserInfoDTO;
import com.codecool.stackoverflowtw.dao.user.model.Role;
import com.codecool.stackoverflowtw.service.QuestionService;
import com.codecool.stackoverflowtw.service.user.AccessControlService;
import com.codecool.stackoverflowtw.service.user.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@CrossOrigin(origins = "http://localhost:5000")
@RestController
@RequestMapping("api/questions")
public class QuestionController {
  private final QuestionService questionService;
  private final TokenService tokenService;
  private final AccessControlService accessControlService;

  private final Logger logger;

  @Autowired
  public QuestionController(QuestionService questionService, TokenService tokenService, AccessControlService accessControlService) {
    this.questionService = questionService;
    this.tokenService = tokenService;
    this.accessControlService = accessControlService;
    logger = LoggerFactory.getLogger(this.getClass());
  }

  @GetMapping("/all")
  public ResponseEntity<?> getAllQuestions() {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", HttpStatus.OK.value(),
        "data", questionService.getAllQuestions()));
    } catch (Exception e) {
      logger.error(e.getMessage(), e);

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status",
        HttpStatus.BAD_REQUEST.value(), "error", "Failed to retrieve questions."));
    }
  }
/*
    @GetMapping("/{id}")
    public QuestionDTO getQuestionById(@PathVariable int id) {
        return null;
    }
 */

  @PostMapping("/")
  public ResponseEntity<?> addNewQuestion(Map<String, String> body) {
    try {
      long userid;

      try {
        //verify token, get userid
        //TODO: read this as Cookie instead
        String sessionToken = body.get("sessionToken");
        TokenUserInfoDTO userInfo = tokenService.verify(sessionToken);
        userid = userInfo.userid();
        //verify user role in the database
        if (!accessControlService.verifyRoleOfUser(userid, Role.USER)) {
          throw new RuntimeException("Unauthorized");
        }
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
          Map.of("status", HttpStatus.UNAUTHORIZED.value(),
            "error", "Unauthorized"));
      }

      questionService.addNewQuestion(new NewQuestionDTO(userid,
        body.get("title"), body.get("content")));

      return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", HttpStatus.OK.value(),
        "message", "Question added successfully"));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status",
        HttpStatus.BAD_REQUEST.value(), "error", "Failed to add question"));
    }
  }

  @DeleteMapping("/{id}")
  public boolean deleteQuestionById(@PathVariable int id) {
    return false;
  }

}
