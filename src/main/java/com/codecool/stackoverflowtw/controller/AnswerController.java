package com.codecool.stackoverflowtw.controller;

import com.codecool.stackoverflowtw.controller.dto.answer.AnswerResponseDetailsDTO;
import com.codecool.stackoverflowtw.controller.dto.answer.NewAnswerDTO;
import com.codecool.stackoverflowtw.controller.dto.answer.UpdateAnswerDTO;
import com.codecool.stackoverflowtw.service.answer.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/answers")
public class AnswerController {
    private final AnswerService answerService;

    @Autowired
    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    //Create an answer
    @PostMapping
    public ResponseEntity<String> createAnswer(@RequestBody NewAnswerDTO newAnswerDTO) {
        try {
            answerService.createAnswer(newAnswerDTO);
            return ResponseEntity.ok("Answer created successfully");
        } catch (SQLException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //update an answer
    @PutMapping
    public ResponseEntity<String> updateAnswer(@RequestBody UpdateAnswerDTO updateAnswerDTO) {
        try {
            answerService.updateAnswer(updateAnswerDTO);
            return ResponseEntity.ok("Answer updated successfully");
        } catch (SQLException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //delete an answer
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAnswer(@PathVariable long id) {
        try {
            answerService.deleteAnswer(id);
            return ResponseEntity.ok("Answer deleted successfully");
        } catch (SQLException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //TODO: get answers by question ID
    //Is it needed for our app? If it is, I don't have any idea for it sadly
    @GetMapping
    public ResponseEntity<List<AnswerResponseDetailsDTO>> getAnswerByQuestionId(@PathVariable long questionId) {
        //TODO: implement if needed. (let's discuss this)
        return null;
    }

}
