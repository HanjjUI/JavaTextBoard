# 掲示板アプリ（Spring Boot）

## 概要

JavaとSpring Bootを用いて掲示板アプリを開発しました。

ユーザー登録・ログイン機能を実装し、認証されたユーザーのみが投稿の作成・編集・削除を行えるように制御しています。
また、データベースと連携し、実際に動作するWebアプリケーションとして構築しました。



## 使用技術

* Java 17
* Spring Boot
* Spring Data JPA
* PostgreSQL（Railway）
* HTML / CSS / JavaScript
* Lombok
* Maven



## 主な機能

* ユーザー登録
* ログイン / ログアウト（セッション管理）
* 投稿作成 / 一覧表示 / 詳細表示
* 投稿編集 / 削除（投稿者のみ可能）
* データベース連携（CRUD処理）



## 工夫した点

* Controller / Service / Repository に責務を分けて実装しました
* Entityを直接返さず、DTOを用いてデータの受け渡しを行いました
* パスワードはBCryptでハッシュ化して安全に保存しています
* HttpSessionを利用してログイン状態を管理しています
* 投稿者本人のみ編集・削除が可能となるよう認可制御を実装しました



## デプロイURL

https://javatextboard-production.up.railway.app



## 今後の改善点

* 検索機能（バックエンド連携予定）
* バリデーション処理の強化
* 例外処理の整理
* UI/UXの改善
* ページネーションの追加
* セキュリティ設定の改善



## 学習・成果

本プロジェクトを通して、Spring Bootにおける基本的な構成（Controller / Service / Repository）や、
CRUD処理、認証・認可、データベース連携の一連の流れを実践的に理解しました。
設計から実装、デプロイまで一通りの開発プロセスを経験しています。
