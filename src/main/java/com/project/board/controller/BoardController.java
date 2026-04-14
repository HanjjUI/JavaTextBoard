package com.project.board.controller;

import com.project.board.dto.BoardDto;
import com.project.board.service.BoardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 掲示板に関するAPIコントローラーです
@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardService service;

    public BoardController(BoardService service){
        this.service = service;
    }

    // 投稿を作成するメソッドです
    @PostMapping("/write")
    public String write(@RequestBody BoardDto dto, HttpSession session){

        String user = (String)session.getAttribute("loginUser");

        // ログインしていない場合は投稿できないようにしています
        if(user == null){
            return "LOGIN_REQUIRED";
        }

        // 入力確認用のログです
        System.out.println("title=" + dto.getTitle());

        service.save(dto, user);

        return "OK";
    }

    // 投稿詳細を取得するメソッドです
    @GetMapping("/{id}")
    public BoardDto detail(@PathVariable Long id){
        return service.findById(id);
    }

    // 投稿一覧を取得するメソッドです
    @GetMapping("/list")
    public List<BoardDto> list(){
        return service.findAll(PageRequest.of(0, 10), "", "")
                .getContent();
    }
}
