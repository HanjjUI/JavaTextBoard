package com.project.board.controller;

import com.project.board.entity.User;
import com.project.board.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

// ユーザー関連APIコントローラー
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service){
        this.service = service;
    }

    // 会員登録処理
    @PostMapping("/signup")
    public String signup(String u, String p){

        try {
            service.signup(u, p);
            return "OK";
        } catch (Exception e){
            return "DUPLICATE";
        }
    }

    // ログイン処理
    @PostMapping("/login")
    public String login(String u, String p, HttpSession session){

        User user = service.login(u, p);

        if(user != null){
            session.setAttribute("loginUser", user.getUsername());
            return "OK";
        }

        return "FAIL";
    }

    // ログアウト処理
    @PostMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "OK";
    }

    // ログイン状態確認
    @GetMapping("/me")
    public String me(HttpSession session){

        String user = (String)session.getAttribute("loginUser");

        if(user == null){
            return "";
        }

        return user;
    }
}