package com.codecool.stackoverflowtw.controller;

import com.codecool.stackoverflowtw.controller.dto.answer.NewAnswerDTO;
import com.codecool.stackoverflowtw.controller.dto.answer.UpdateAnswerDTO;
import com.codecool.stackoverflowtw.service.answer.AnswerService;
import com.codecool.stackoverflowtw.service.user.AccessControlService;
import com.codecool.stackoverflowtw.service.user.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> createAnswer(@RequestBody NewAnswerDTO newAnswerDTO) {
        try {
            answerService.createAnswer(newAnswerDTO);
            return handleOkMessage("Answer created successfully");
        } catch (Exception e) {
            return handleBadRequest("Failed to create answer", e);
        }
    }

    //update an answer
    @PatchMapping("")
    public ResponseEntity<?> updateAnswer(@RequestBody UpdateAnswerDTO updateAnswerDTO) {
        try {
            answerService.updateAnswer(updateAnswerDTO);
            return handleOkMessage("Answer updated successfully");
        } catch (Exception e) {
            return handleBadRequest("Failed to update answer", e);
        }
    }

    //delete an answer
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnswer(@PathVariable Long id) {
        try {
            answerService.deleteAnswer(id);
            return handleOkMessage("Answer deleted successfully");
        } catch (Exception e) {
            return handleBadRequest("Failed to update answer", e);
        }
    }


    //TODO: get all answers by question ID
    //Is it needed for our app? If it is, I don't have any idea for it sadly
    @GetMapping("/{questionId}")
    public ResponseEntity<?> getAnswerByQuestionId(@PathVariable Long questionId) {
        try {
            return handleOkData("Fetched answers for question", answerService.getAnswerByQuestionId(questionId));
        } catch (Exception e) {
            return handleBadRequest("Failed to fetch answers for this question", e);
        }
    }


}
