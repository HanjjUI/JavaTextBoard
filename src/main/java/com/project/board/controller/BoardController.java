package com.project.board.controller;

import com.project.board.dto.BoardDto;
import com.project.board.service.BoardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 掲示板APIコントローラー
@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardService service;

    public BoardController(BoardService service){
        this.service = service;
    }

    // 投稿作成
    @PostMapping("/write")
    public String write(@RequestBody BoardDto dto, HttpSession session){

        String user = (String)session.getAttribute("loginUser");

        if(user == null){
            return "LOGIN_REQUIRED";
        }
        System.out.println("title=" + dto.getTitle()); // 🔥 확인용

        service.save(dto, user);

        return "OK";
    }
    
    // 投稿一覧取得
    @GetMapping("/list")
    public List<BoardDto> list(){
        return service.findAll(PageRequest.of(0, 10), "", "")
                .getContent();
    }
}