package com.project.board.controller;

import com.project.board.entity.User;
import com.project.board.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService s;

    public UserController(UserService s){ this.s=s; }

    @PostMapping("/signup")
    public void signup(String u,String p){ s.signup(u,p); }

    @PostMapping("/login")
    public String login(String u,String p,HttpSession session){
        User user = s.login(u,p);
        if(user!=null){
            session.setAttribute("loginUser", user.getUsername());
            return "OK";
        }
        return "FAIL";
    }

    @GetMapping("/me")
    public String me(HttpSession session){
        return (String)session.getAttribute("loginUser");
    }

    @PostMapping("/logout")
    public void logout(HttpSession session){
        session.invalidate();
    }
}