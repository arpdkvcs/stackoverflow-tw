package com.codecool.stackoverflowtw.controller;

import com.codecool.stackoverflowtw.controller.dto.question.QuestionResponseDTO;
import com.codecool.stackoverflowtw.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;


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

/*
    @PostMapping("/")
    public int addNewQuestion(@RequestBody NewQuestionDTO question) {
        return 0;
    }

    @DeleteMapping("/{id}")
    public boolean deleteQuestionById(@PathVariable int id) {
        return false;
    }

 */
}
