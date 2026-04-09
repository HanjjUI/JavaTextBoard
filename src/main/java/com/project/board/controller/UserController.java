package com.project.board.controller;

import com.project.board.entity.User;
import com.project.board.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

/**
 * ユーザー関連APIコントローラー
 * - 認証（ログイン / ログアウト）
 * - セッション管理
 * - 会員登録
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service){
        this.service = service;
    }

    /**
     * 会員登録
     * @param u username
     * @param p password
     */
    @PostMapping("/signup")
    public String signup(@RequestParam String u, @RequestParam String p){

        System.out.println("[SIGNUP] username = " + u + ", p=" + p);

        try {
            service.signup(u, p);
            return "OK";
        } catch (Exception e) {
        	e.printStackTrace();
        	return "ERROR";
        }
    }

    /**
     * ログイン
     * @param u username
     * @param p password
     */
    @PostMapping("/login")
    public String login(String u, String p, HttpSession session){

        System.out.println("[LOGIN TRY] " + u);

        User user = service.login(u, p);

        if(user != null){
            // 🔥 세션 저장 (핵심)
            session.setAttribute("loginUser", user.getUsername());

            System.out.println("[LOGIN SUCCESS] " + u);
            return "OK";
        }

        System.out.println("[LOGIN FAIL] " + u);
        return "FAIL";
    }

    /**
     * ログアウト
     */
    @PostMapping("/logout")
    public String logout(HttpSession session){

        session.invalidate(); // 🔥 세션 완전 삭제

        System.out.println("[LOGOUT]");
        return "OK";
    }

    /**
     * ログイン状態確認
     * 프론트에서 로그인 여부 체크용
     */
    @GetMapping("/me")
    public String me(HttpSession session){

        String user = (String) session.getAttribute("loginUser");

        // 🔥 중요: null 명확하게 반환
        if(user == null){
            return "null";
        }

        return user;
    }
}