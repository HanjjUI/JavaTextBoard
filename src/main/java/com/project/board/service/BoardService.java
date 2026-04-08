package com.project.board.service;

import com.project.board.dto.BoardDto;
import com.project.board.entity.Board;
import com.project.board.repository.BoardRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 掲示板サービス（ビジネスロジック担当）
@Service
public class BoardService {

    private final BoardRepository repo;

    // コンストラクタ注入
    public BoardService(BoardRepository repo){
        this.repo = repo;
    }

    // 投稿一覧取得（検索 + ページング）
    public Page<BoardDto> findAll(Pageable p, String k, String a){

        // null対策
        if(k == null) k = "";
        if(a == null) a = "";

        return repo.findByTitleContainingAndAuthorContaining(k, a, p)
                .map(this::toDto);
    }

    // 投稿詳細取得（閲覧数 증가）
    @Transactional
    public BoardDto findById(Long id){

        Board b = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("投稿が存在しません"));

        // 조회수 증가
        b.increaseViewCount();

        return toDto(b);
    }

    // 投稿作成
    public BoardDto save(BoardDto d, String user){

        Board b = Board.create(
                d.getTitle(),
                d.getContent(),
                user
        );

        return toDto(repo.save(b));
    }

    // 投稿更新（작성자만 가능）
    @Transactional
    public BoardDto update(Long id, BoardDto d, String user){

        Board b = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("投稿が存在しません"));

        // 권한 체크
        if(!b.getAuthor().equals(user)){
            throw new RuntimeException("権限がありません");
        }

        b.update(d.getTitle(), d.getContent());

        return toDto(b);
    }

    // 投稿削除（작성자만 가능）
    public void delete(Long id, String user){

        Board b = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("投稿が存在しません"));

        if(!b.getAuthor().equals(user)){
            throw new RuntimeException("権限がありません");
        }

        repo.delete(b);
    }

    // Entity → DTO変換
    private BoardDto toDto(Board b){
        return BoardDto.builder()
                .id(b.getId())
                .title(b.getTitle())
                .content(b.getContent())
                .author(b.getAuthor())
                .createdAt(b.getCreatedAt())
                .viewCount(b.getViewCount())
                .build();
    }
}