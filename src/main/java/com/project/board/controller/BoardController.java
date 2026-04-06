package com.project.board.controller;

import com.project.board.dto.BoardDto;
import com.project.board.service.BoardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 글 작성
    @PostMapping
    public BoardDto create(@RequestBody BoardDto dto) {
        return boardService.save(dto);
    }

    // 전체 조회
    @GetMapping
    public List<BoardDto> list() {
        return boardService.findAll();
    }

    // 상세 조회
    @GetMapping("/{id}")
    public BoardDto detail(@PathVariable Long id) {
        return boardService.findById(id);
    }

    // 삭제
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        boardService.delete(id);
    }
}