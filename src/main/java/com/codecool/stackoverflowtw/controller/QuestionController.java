package com.codecool.stackoverflowtw.controller;

import com.codecool.stackoverflowtw.controller.dto.question.NewQuestionDTO;
import com.codecool.stackoverflowtw.controller.dto.question.UpdateQuestionDTO;
import com.codecool.stackoverflowtw.service.QuestionService;
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

    private final Logger logger;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuestionById(@PathVariable int id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", HttpStatus.OK.value(),
                    "data", questionService.getQuestionById(id)));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status",
                    HttpStatus.BAD_REQUEST.value(), "error", "Failed to retrieve question."));
        }
    }

    @GetMapping("/search/questions/{searchQuery}")
    public ResponseEntity<?> getQuestionsByTitle(@PathVariable String searchQuery) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", HttpStatus.OK.value(),
                    "data", questionService.getQuestionsByTitle(searchQuery)));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status",
                    HttpStatus.BAD_REQUEST.value(), "error", "Failed to perform search."));
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> addNewQuestion(@RequestBody NewQuestionDTO question) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", HttpStatus.OK.value(),
                    "data", questionService.addNewQuestion(question)));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status",
                    HttpStatus.BAD_REQUEST.value(), "error", "Failed to post new question."));
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> updateQuestion(@RequestBody UpdateQuestionDTO question) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", HttpStatus.OK.value(),
                    "data", questionService.updateQuestion(question)));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status",
                    HttpStatus.BAD_REQUEST.value(), "error", "Failed to update question."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuestionById(@PathVariable long id) {
        try {
            questionService.deleteQuestionById(id);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", HttpStatus.OK.value(),
                    "message", "Question has been deleted."));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status",
                    HttpStatus.BAD_REQUEST.value(), "error", "Failed to delete question."));
        }
    }
}
