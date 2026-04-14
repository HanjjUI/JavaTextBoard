package com.project.board.service;

import com.project.board.dto.BoardDto;
import com.project.board.entity.Board;
import com.project.board.repository.BoardRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 掲示板サービスです
// 投稿に関するビジネスロジックを担当します
@Service
public class BoardService {

    private final BoardRepository repo;

    // コンストラクタでRepositoryを受け取ります
    public BoardService(BoardRepository repo){
        this.repo = repo;
    }

    // 投稿一覧を取得するメソッドです
    // タイトルと投稿者名で検索できて、ページングにも対応しています
    public Page<BoardDto> findAll(Pageable p, String k, String a){

        // null のままだとエラーになりやすいので空文字にします
        if(k == null) k = "";
        if(a == null) a = "";

        return repo.findByTitleContainingAndAuthorContaining(k, a, p)
                .map(this::toDto);
    }

    // 投稿詳細を取得するメソッドです
    // 詳細画面を開いたときに閲覧数も1増やします
    @Transactional
    public BoardDto findById(Long id){

        Board b = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("投稿が存在しません"));

        // 閲覧数を増やします
        b.increaseViewCount();

        return toDto(b);
    }

    // 新しい投稿を保存するメソッドです
    public BoardDto save(BoardDto d, String user){

        Board b = Board.create(
                d.getTitle(),
                d.getContent(),
                user
        );

        return toDto(repo.save(b));
    }

    // 投稿を修正するメソッドです
    // 投稿した本人だけ修正できるようにしています
    @Transactional
    public BoardDto update(Long id, BoardDto d, String user){

        Board b = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("投稿が存在しません"));

        // 投稿者本人かどうかを確認します
        if(!b.getAuthor().equals(user)){
            throw new RuntimeException("権限がありません");
        }

        b.update(d.getTitle(), d.getContent());

        return toDto(b);
    }

    // 投稿を削除するメソッドです
    // こちらも投稿した本人だけ削除できます
    public void delete(Long id, String user){

        Board b = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("投稿が存在しません"));

        // 投稿者本人かどうかを確認します
        if(!b.getAuthor().equals(user)){
            throw new RuntimeException("権限がありません");
        }

        repo.delete(b);
    }

    // Entity を DTO に変換するためのメソッドです
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
