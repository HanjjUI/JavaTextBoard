package com.project.board.controller;

import com.project.board.entity.User;
import com.project.board.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

/**
 * ユーザー関連のAPIコントローラーです
 * ログイン、ログアウト、会員登録などを担当します
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service){
        this.service = service;
    }

    /**
     * 会員登録をするメソッドです
     * u はユーザー名、p はパスワードです
     */
    @PostMapping("/signup")
    public String signup(@RequestParam String u, @RequestParam String p){
        try {
            service.signup(u, p);
            return "OK";
        } catch (RuntimeException e) {
            e.printStackTrace();

            if ("既に存在するユーザーです".equals(e.getMessage())) {
                return "DUPLICATE";
            }

            return "ERROR";
        }
    }

    /**
     * ログインをするメソッドです
     * ログイン成功時はセッションにユーザー情報を保存します
     */
    @PostMapping("/login")
    public String login(String u, String p, HttpSession session){

        System.out.println("[LOGIN TRY] " + u);

        User user = service.login(u, p);

        if(user != null){
            // ログインしたユーザー名をセッションに保存します
            session.setAttribute("loginUser", user.getUsername());

            System.out.println("[LOGIN SUCCESS] " + u);
            return "OK";
        }

        System.out.println("[LOGIN FAIL] " + u);
        return "FAIL";
    }

    /**
     * ログアウトをするメソッドです
     * セッション情報を削除します
     */
    @PostMapping("/logout")
    public String logout(HttpSession session){

        // セッションを無効にしてログアウト状態にします
        session.invalidate();

        System.out.println("[LOGOUT]");
        return "OK";
    }

    /**
     * 現在ログインしているユーザーを確認するメソッドです
     * フロント側でログイン状態を確認するときに使います
     */
    @GetMapping("/me")
    public String me(HttpSession session){

        String user = (String) session.getAttribute("loginUser");

        // ログインしていない場合は null を返します
        if(user == null){
            return "null";
        }

        return user;
    }
}
