package com.project.board.controller;

import com.project.board.dto.BoardDto;
import com.project.board.service.BoardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardService s;

    public BoardController(BoardService s){ this.s=s; }

    @GetMapping
    public Page<BoardDto> getAll(Pageable p,
        @RequestParam(required=false) String keyword,
        @RequestParam(required=false) String author){
        return s.findAll(p,keyword,author);
    }

    @GetMapping("/{id}")
    public BoardDto get(@PathVariable Long id){
        return s.findById(id);
    }

    @PostMapping
    public BoardDto create(@RequestBody BoardDto d,HttpSession session){
        return s.save(d,(String)session.getAttribute("loginUser"));
    }

    @PutMapping("/{id}")
    public BoardDto update(@PathVariable Long id,
        @RequestBody BoardDto d,HttpSession session){
        return s.update(id,d,(String)session.getAttribute("loginUser"));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id,HttpSession session){
        s.delete(id,(String)session.getAttribute("loginUser"));
    }
}