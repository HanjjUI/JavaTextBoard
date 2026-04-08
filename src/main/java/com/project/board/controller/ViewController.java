package com.project.board.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

// 画面表示用コントローラー
@Controller
public class ViewController {

	@GetMapping("/")
	@ResponseBody
	public String index(){
	    return "HELLO";
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
    
    @GetMapping("/test")
    public String test(){
        return "OK";
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
