package com.project.board.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/index")
    public String root(){
        return "index";
    }

    @GetMapping("/login")
    public String login(HttpSession session){

        if(session.getAttribute("loginUser") != null){
            return "redirect:/";
        }

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

        if(session.getAttribute("loginUser") == null){
            return "redirect:/login";
        }

        return "board-write";
    }
}