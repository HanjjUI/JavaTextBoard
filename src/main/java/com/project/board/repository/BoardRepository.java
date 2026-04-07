package com.project.board.repository;

import com.project.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findByTitleContainingAndAuthorContaining(
            String title,
            String author,
            Pageable pageable
    );
}