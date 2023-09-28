package com.codecool.stackoverflowtw.controller;

import com.codecool.stackoverflowtw.controller.dto.question.NewQuestionDTO;
import com.codecool.stackoverflowtw.controller.dto.question.UpdateQuestionDTO;
import com.codecool.stackoverflowtw.controller.dto.user.TokenUserInfoDTO;
import com.codecool.stackoverflowtw.service.QuestionService;
import com.codecool.stackoverflowtw.service.user.AccessControlService;
import com.codecool.stackoverflowtw.service.user.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;


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
  public ResponseEntity<?> getQuestionById(@PathVariable Long id) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", HttpStatus.OK.value(),
        "data", questionService.getQuestionById(id)));
    } catch (Exception e) {
      return handleBadRequest("Failed to retrieve question.", e);
    }
  }

  @GetMapping("/search/{searchQuery}")
  public ResponseEntity<?> getQuestionsByTitle(@PathVariable String searchQuery) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", HttpStatus.OK.value(),
        "data", questionService.getQuestionsByTitle(searchQuery)));
    } catch (Exception e) {
      return handleBadRequest("Failed to perform search.", e);
    }
  }

  @PostMapping("")
  public ResponseEntity<?> addNewQuestion(
          HttpServletRequest request, @RequestBody NewQuestionDTO newQuestionDTO) {
    try {
      /*
      Optional<TokenUserInfoDTO> userInfo = verifyToken(request);

      if (userInfo.isPresent()) {
        NewQuestionDTO question = new NewQuestionDTO(
                userInfo.get().userid(),
                body.get("title").toString(),
                body.get("content").toString()
        );
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", HttpStatus.OK.value(),
                "data", questionService.addNewQuestion(question)));
      } else {
        return handleUnauthorized("Unauthorized", null);
      }

       */

      return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", HttpStatus.OK.value(),
              "data", questionService.addNewQuestion(newQuestionDTO)));

    } catch (Exception e) {
      return handleBadRequest("Failed to post new question.", e);
    }
  }

  @PatchMapping("")
  public ResponseEntity<?> updateQuestion(
    HttpServletRequest request, @RequestBody UpdateQuestionDTO updateQuestionDTO) {
    try {
      /*
      Optional<TokenUserInfoDTO> userInfo = verifyToken(request);
      if (userInfo.isPresent()) {

        UpdateQuestionDTO question = new UpdateQuestionDTO((long) body.get("id"),
          userInfo.get().userid(),
          body.get("title").toString(), body.get("content").toString(),
          (long) body.get("acceptedAnswerId"));
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", HttpStatus.OK.value(),
          "data", questionService.updateQuestion(question)));

      } else {
        return handleUnauthorized("Unauthorized", null);
      }

       */

      return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", HttpStatus.OK.value(),
              "data", questionService.updateQuestion(updateQuestionDTO)));

    } catch (Exception e) {
      return handleBadRequest("Failed to update question.", e);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteQuestionById(@PathVariable Long id) {
    try {
      questionService.deleteQuestionById(id);
      return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", HttpStatus.OK.value(),
        "message", "Question has been deleted."));
    } catch (Exception e) {
      return handleBadRequest("Failed to delete question.", e);
    }
  }
}
