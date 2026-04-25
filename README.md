# JavaTextBoard

Spring Bootで作成したシンプルなテキスト掲示板アプリです。  
ユーザー登録とログイン後に投稿を作成でき、投稿詳細を開くと閲覧数が増加します。

## 概要

このプロジェクトは、Spring Bootを使って作成した掲示板アプリです。  
ユーザー登録、ログイン、ログアウト、投稿の作成・編集・削除・一覧表示・詳細表示といった基本機能を実装しています。

認証はセッションベースで管理しており、投稿の作成・編集・削除はログイン済みユーザーのみ可能です。  
また、編集と削除は投稿者本人のみ実行できるように制御しています。

## 主な機能

- ユーザー登録
- ログイン / ログアウト
- セッションによるログイン状態の確認
- 投稿一覧表示
- 投稿詳細表示
- 投稿作成
- 投稿編集
- 投稿削除
- 閲覧数の自動増加

## 使用技術

- Java 17
- Spring Boot 3.2.5
- Spring Data JPA
- Spring Security
- Thymeleaf
- PostgreSQL
- H2 Database
- Lombok
- Maven

## プロジェクト構成

```text
src
├─ main
│  ├─ java/com/project/board
│  │  ├─ config
│  │  ├─ controller
│  │  ├─ dto
│  │  ├─ entity
│  │  ├─ repository
│  │  └─ service
│  └─ resources
│     ├─ templates
│     └─ application.properties
└─ test
```

## 動作仕様

- ログイン成功時に `HttpSession` へ `loginUser` を保存します。
- 投稿の作成・編集・削除はログイン済みユーザーのみ可能です。
- 投稿の編集・削除は投稿者本人のみ可能です。
- 投稿詳細を開くと `viewCount` が 1 増加します。
- Spring Security のデフォルトログインフォームは使わず、全リクエストを許可したうえでセッションベースの認証ロジックを適用しています。

## 画面ルート

- `/` または `/index` : トップページ
- `/login` : ログインページ
- `/signup` : 新規登録ページ
- `/board` : 投稿一覧ページ
- `/board/write` : 投稿作成ページ

## API

### ユーザーAPI

- `POST /user/signup`
  - パラメータ: `u`, `p`
  - レスポンス: `OK`, `DUPLICATE`, `ERROR`
- `POST /user/login`
  - パラメータ: `u`, `p`
  - レスポンス: `OK`, `FAIL`
- `POST /user/logout`
  - レスポンス: `OK`
- `GET /user/me`
  - レスポンス: ログイン中のユーザー名、未ログイン時は `null`

### 投稿API

- `GET /board/list`
  - 投稿一覧を10件取得
- `GET /board/{id}`
  - 投稿詳細を取得
- `POST /board/write`
  - リクエストボディ: `BoardDto`
  - レスポンス: `OK`, `LOGIN_REQUIRED`
- `PUT /board/{id}`
  - リクエストボディ: `BoardDto`
  - レスポンス: `OK`, `LOGIN_REQUIRED`
- `DELETE /board/{id}`
  - レスポンス: `OK`, `LOGIN_REQUIRED`

## ローカル実行方法

### 1. Java 17 を準備

Java 17 がインストールされている必要があります。

### 2. 環境変数を設定

このプロジェクトは、PostgreSQL の接続情報を環境変数から取得します。

必要な環境変数:

- `PGHOST`
- `PGPORT`
- `PGDATABASE`
- `PGUSER`
- `PGPASSWORD`

`application.properties` では以下のように設定されています。

```properties
spring.datasource.url=jdbc:postgresql://${PGHOST}:${PGPORT}/${PGDATABASE}
spring.datasource.username=${PGUSER}
spring.datasource.password=${PGPASSWORD}
```

### 3. アプリを実行

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

または

```powershell
.\mvnw.cmd test
.\mvnw.cmd package
java -jar target\board-0.0.1-SNAPSHOT.jar
```

起動後のアクセス先:

```text
http://localhost:8080
```

## デプロイ

Railway へのデプロイを想定しており、ポートは以下の設定を使用しています。

```properties
server.port=${PORT:8080}
```

## データモデル

### User

- `id`
- `username`
- `password`

パスワードは `BCryptPasswordEncoder` でハッシュ化して保存します。

### Board

- `id`
- `title`
- `content`
- `author`
- `createdAt`
- `viewCount`

`createdAt` は `@PrePersist` によって自動設定されます。

## テスト

基本的な Spring Boot テストクラスが含まれています。

```powershell
.\mvnw.cmd test
```

## 補足

- `application.properties` は PostgreSQL を前提に設定されています。
- `pom.xml` には H2 依存関係も含まれていますが、現在のデフォルト設定は PostgreSQL です。
- 既存の README と一部ソースコメントには文字化けがあったため、README は現在のコード実装に合わせて日本語で再整理しています。
