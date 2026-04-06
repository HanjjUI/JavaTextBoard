package com.project.board.controller;

import com.project.board.dto.BoardDto;
import com.project.board.service.BoardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * BoardController
 *
 * 역할:
 * - HTTP 요청 처리 (REST API)
 *
 * 엔드포인트:
 * - POST   /board        : 게시글 생성
 * - GET    /board        : 전체 조회
 * - GET    /board/{id}   : 단일 조회
 * - DELETE /board/{id}   : 삭제
 */
@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    // 생성자 주입
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * 게시글 생성
     */
    @PostMapping
    public BoardDto create(@RequestBody BoardDto dto) {
        return boardService.save(dto);
    }

    /**
     * 전체 조회
     */
    @GetMapping
    public List<BoardDto> getAll() {
        return boardService.findAll();
    }

    /**
     * 단일 조회
     */
    @GetMapping("/{id}")
    public BoardDto get(@PathVariable Long id) {
        return boardService.findById(id);
    }

    /**
     * 게시글 삭제
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        boardService.delete(id);
    }
    
    @GetMapping("/")
    public String home() {
        return "server is running";
    }
}