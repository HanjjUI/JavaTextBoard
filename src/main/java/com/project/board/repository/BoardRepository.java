package com.project.board.repository;

import com.project.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByTitleContaining(String keyword);

    List<Board> findByAuthor(String author);
}