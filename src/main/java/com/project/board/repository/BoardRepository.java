package com.project.board.repository;

import com.project.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

// 掲示板Repository（DBアクセス担当）
public interface BoardRepository extends JpaRepository<Board, Long> {

    // タイトルと作成者で部分一致検索 + ページング
    Page<Board> findByTitleContainingAndAuthorContaining(
            String title,
            String author,
            Pageable pageable
    );

    // タイトルのみ検索（オプション）
    Page<Board> findByTitleContaining(
            String title,
            Pageable pageable
    );
}