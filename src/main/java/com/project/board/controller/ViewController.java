package com.project.board.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    // メイン画面を表示します
    @GetMapping("/")
    public String index(){
        return "index";
    }

    // /index でアクセスした場合もメイン画面を表示します
    @GetMapping("/index")
    public String root(){
        return "index";
    }

    // ログイン画面を表示します
    // すでにログインしている場合はメイン画面へ戻します
    @GetMapping("/login")
    public String login(HttpSession session){

        if(session.getAttribute("loginUser") != null){
            return "redirect:/";
        }

        return "login";
    }

    // 会員登録画面を表示します
    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }

    // 掲示板一覧画面を表示します
    @GetMapping("/board")
    public String board(){
        return "board-list";
    }

    // 投稿作成画面を表示します
    // ログインしていない場合はログイン画面へ移動します
    @GetMapping("/board/write")
    public String write(HttpSession session){

        if(session.getAttribute("loginUser") == null){
            return "redirect:/login";
        }

        return "board-write";
    }
}
