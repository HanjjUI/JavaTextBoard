package com.project.board.controller;

import com.project.board.common.SessionNames;
import com.project.board.entity.User;
import com.project.board.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String u, @RequestParam String p) {
        try {
            service.signup(u, p);
            return "OK";
        } catch (IllegalArgumentException e) {
            return "DUPLICATE";
        } catch (RuntimeException e) {
            return "ERROR";
        }
    }

    @PostMapping("/login")
    public String login(String u, String p, HttpSession session) {
        User user = service.login(u, p);

        if (user != null) {
            session.setAttribute(SessionNames.LOGIN_USER, user.getUsername());
            return "OK";
        }

        return "FAIL";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "OK";
    }

    @GetMapping("/me")
    public String me(HttpSession session) {
        String user = (String) session.getAttribute(SessionNames.LOGIN_USER);

        if (user == null) {
            return "null";
        }

        return user;
    }
}
