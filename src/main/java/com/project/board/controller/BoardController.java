package com.project.board.controller;

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

    private final BoardService service;

    public BoardController(BoardService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public List<BoardDto> list(
            @RequestParam(defaultValue = "titleAuthor") String searchType,
            @RequestParam(defaultValue = "") String keyword
    ) {
        return service.findAll(
                        PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "id")),
                        searchType,
                        keyword
                )
                .getContent();
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
        String user = (String) session.getAttribute("loginUser");

        if (user == null) {
            return "LOGIN_REQUIRED";
        }

        service.save(dto, user);
        return "OK";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        String user = (String) session.getAttribute("loginUser");

        if (user == null) {
            return "LOGIN_REQUIRED";
        }

        service.delete(id, user);
        return "OK";
    }

    @PutMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @RequestBody BoardDto dto,
            HttpSession session
    ) {
        String user = (String) session.getAttribute("loginUser");

        if (user == null) {
            return "LOGIN_REQUIRED";
        }

        service.update(id, dto, user);
        return "OK";
    }
}
