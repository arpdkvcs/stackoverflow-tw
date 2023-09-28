package com.codecool.stackoverflowtw.controller;

import com.codecool.stackoverflowtw.controller.dto.answer.NewAnswerDTO;
import com.codecool.stackoverflowtw.controller.dto.answer.UpdateAnswerDTO;
import com.codecool.stackoverflowtw.service.answer.AnswerService;
import com.codecool.stackoverflowtw.service.user.AccessControlService;
import com.codecool.stackoverflowtw.service.user.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/answers")
public class AnswerController extends BaseController {
    private final AnswerService answerService;

  public AnswerController(TokenService tokenService,
                          AccessControlService accessControlService,
                          AnswerService answerService) {
    super(tokenService, accessControlService);
    this.answerService = answerService;
  }

  //Create an answer
    @PostMapping
    public ResponseEntity<?> createAnswer(@RequestBody NewAnswerDTO newAnswerDTO) {
        try {
            answerService.createAnswer(newAnswerDTO);
            return ResponseEntity.ok("Answer created successfully");
        } catch (Exception e) {
            return handleBadRequest("Failed to create answer",e);
        }
    }

    //update an answer
    @PutMapping
    public ResponseEntity<?> updateAnswer(@RequestBody UpdateAnswerDTO updateAnswerDTO) {
        try {
            answerService.updateAnswer(updateAnswerDTO);
            return ResponseEntity.ok("Answer updated successfully");
        } catch (Exception e) {
          return handleBadRequest("Failed to update answer",e);
        }
    }

    //delete an answer
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnswer(@PathVariable long id) {
        try {
            answerService.deleteAnswer(id);
            return ResponseEntity.ok("Answer deleted successfully");
        } catch (Exception e) {
          return handleBadRequest("Failed to update answer",e);
        }
    }

  /*
    //TODO: get answers by question ID
    //Is it needed for our app? If it is, I don't have any idea for it sadly
    @GetMapping
    public ResponseEntity<List<AnswerResponseDetailsDTO>> getAnswerByQuestionId(@PathVariable long questionId) {
        //TODO: implement if needed. (let's discuss this)
        return null;
    }
   */

}
