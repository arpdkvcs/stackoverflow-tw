package com.codecool.stackoverflowtw.controller;

import com.codecool.stackoverflowtw.controller.dto.question.NewQuestionDTO;
import com.codecool.stackoverflowtw.controller.dto.question.UpdateQuestionDTO;
import com.codecool.stackoverflowtw.service.QuestionService;
import com.codecool.stackoverflowtw.service.user.AccessControlService;
import com.codecool.stackoverflowtw.service.user.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@CrossOrigin(origins = "http://localhost:5000")
@RestController
@RequestMapping("api/questions")
public class QuestionController extends BaseController {
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
      return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", HttpStatus.OK.value(),
        "data", questionService.getAllQuestions()));
    } catch (Exception e) {
      return handleBadRequest("Failed to retrieve questions.", e);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getQuestionById(@PathVariable int id) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", HttpStatus.OK.value(),
        "data", questionService.getQuestionById(id)));
    } catch (Exception e) {
      return handleBadRequest("Failed to retrieve question.", e);
    }
  }

  @GetMapping("/search/questions/{searchQuery}")
  public ResponseEntity<?> getQuestionsByTitle(@PathVariable String searchQuery) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", HttpStatus.OK.value(),
        "data", questionService.getQuestionsByTitle(searchQuery)));
    } catch (Exception e) {
      return handleBadRequest("Failed to perform search.", e);
    }
  }

  @PostMapping("/")
  public ResponseEntity<?> addNewQuestion(@RequestBody NewQuestionDTO question) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", HttpStatus.OK.value(),
        "data", questionService.addNewQuestion(question)));
    } catch (Exception e) {
      return handleBadRequest("Failed to post new question.", e);
    }
  }

  @PatchMapping ("/")
  public ResponseEntity<?> updateQuestion(@RequestBody UpdateQuestionDTO question) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", HttpStatus.OK.value(),
        "data", questionService.updateQuestion(question)));
    } catch (Exception e) {
      return handleBadRequest("Failed to update question.", e);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteQuestionById(@PathVariable long id) {
    try {
      questionService.deleteQuestionById(id);
      return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", HttpStatus.OK.value(),
        "message", "Question has been deleted."));
    } catch (Exception e) {
      return handleBadRequest("Failed to delete question.", e);
    }
  }
}
