package com.project.board.controller;

import com.project.board.dto.AiAnswerRequest;
import com.project.board.dto.AiAnswerResponse;
import com.project.board.service.OpenAiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class AiController {

    private final OpenAiService openAiService;

    public AiController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @PostMapping("/answer")
    public AiAnswerResponse answer(@RequestBody AiAnswerRequest request) {
        return new AiAnswerResponse(openAiService.answer(request.getQuestion()));
    }
}
