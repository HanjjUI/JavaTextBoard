package com.project.board.controller;

import com.project.board.common.SessionNames;
import com.project.board.dto.BoardDto;
import com.project.board.service.BoardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/board")
public class BoardController {

    private static final int DEFAULT_PAGE_SIZE = 100;
    private static final String LOGIN_REQUIRED = "LOGIN_REQUIRED";

    private final BoardService service;

    public BoardController(BoardService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public List<BoardDto> list(
            @RequestParam(defaultValue = "titleAuthor") String searchType,
            @RequestParam(defaultValue = "") String keyword
    ) {
        PageRequest pageRequest = PageRequest.of(
                0,
                DEFAULT_PAGE_SIZE,
                Sort.by(Sort.Direction.DESC, "id")
        );

        return service.findAll(pageRequest, searchType, keyword).getContent();
    }

    @GetMapping("/{id}")
    public BoardDto detail(
            @PathVariable Long id,
            @RequestParam(defaultValue = "true") boolean increaseViewCount
    ) {
        return service.findById(id, increaseViewCount);
    }

    @PostMapping("/write")
    public String write(@RequestBody BoardDto dto, HttpSession session) {
        String user = getLoginUser(session);

        if (user == null) {
            return LOGIN_REQUIRED;
        }

        service.save(dto, user);
        return "OK";
    }

    @PutMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @RequestBody BoardDto dto,
            HttpSession session
    ) {
        String user = getLoginUser(session);

        if (user == null) {
            return LOGIN_REQUIRED;
        }

        service.update(id, dto, user);
        return "OK";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        String user = getLoginUser(session);

        if (user == null) {
            return LOGIN_REQUIRED;
        }

        service.delete(id, user);
        return "OK";
    }

    private String getLoginUser(HttpSession session) {
        return (String) session.getAttribute(SessionNames.LOGIN_USER);
    }
}
