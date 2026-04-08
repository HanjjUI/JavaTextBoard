package com.project.board.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 画面表示用コントローラー
@Controller
public class ViewController {

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }

    @GetMapping("/board")
    public String board(){
        return "board-list";
    }

    @GetMapping("/board/write")
    public String write(HttpSession session){

        // ログインチェック
        if(session.getAttribute("loginUser") == null){
            return "redirect:/login";
        }

        return "board-write";
    }
}