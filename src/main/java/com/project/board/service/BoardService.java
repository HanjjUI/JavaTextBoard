package com.project.board.service;

import com.project.board.entity.Board;
import com.project.board.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    // 생성자 주입 (Spring 기본 패턴)
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    // 글 저장
    public Board save(Board board) {
        return boardRepository.save(board);
    }

    // 전체 조회
    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    // 단일 조회
    public Board findById(Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    // 삭제
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }
}