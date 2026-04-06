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

    @PostMapping
    public BoardDto create(@RequestBody BoardDto dto) {
        return boardService.save(dto);
    }

    @GetMapping
    public List<BoardDto> getAll() {
        return boardService.findAll();
    }

    @GetMapping("/{id}")
    public BoardDto get(@PathVariable Long id) {
        return boardService.findById(id);
    }

    @PutMapping("/{id}")
    public BoardDto update(@PathVariable Long id, @RequestBody BoardDto dto) {
        return boardService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        boardService.delete(id);
    }

    // 검색
    @GetMapping("/search")
    public List<BoardDto> search(@RequestParam String keyword) {
        return boardService.searchByTitle(keyword);
    }

    @GetMapping("/author")
    public List<BoardDto> searchAuthor(@RequestParam String author) {
        return boardService.searchByAuthor(author);
    }
}