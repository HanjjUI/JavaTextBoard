package com.project.board.controller;

import com.project.board.common.SessionNames;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping({"/", "/index", "/board"})
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        if (session.getAttribute(SessionNames.LOGIN_USER) != null) {
            return "redirect:/";
        }

        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/board/write")
    public String write(HttpSession session) {
        if (session.getAttribute(SessionNames.LOGIN_USER) == null) {
            return "redirect:/login";
        }

        return "board-write";
    }
}
