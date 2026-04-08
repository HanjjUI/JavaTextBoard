# 📝 掲示板アプリ

## 📌 概要
Spring Bootを使用して作成したシンプルな掲示板アプリです。  
会員登録・ログイン・投稿機能を実装しました。

---

## 🔧 使用技術
- Java (Spring Boot)
- Spring Data JPA
- PostgreSQL（Railway）
- HTML / JavaScript
- Lombok

---

## 🚀 機能一覧
- 会員登録
- ログイン / ログアウト
- 投稿作成
- 投稿一覧表示
- 投稿編集 / 削除
- 検索機能（タイトル・作成者）
- ページネーション

---

## 🖥️ 画面
- メイン画面
- ログイン画面
- 会員登録画面
- 投稿一覧画面
- 投稿作成画面

---

## 🔗 デプロイURL
https://your-app.railway.app

---

## 💡 工夫した点
- ControllerとServiceを分離して設計しました  
- Entityを直接返さず、DTOを使用しました  
- パスワードはBCryptで暗号化しました  
- セッションを使ってログイン状態を管理しました  

---

## ⚠️ 今後の改善点
- Spring Securityの導入  
- デザインの改善  
- 例外処理の強化  
- API設計の改善  

---

## 🙋‍♂️ 学習したこと
このプロジェクトを通じて、  
Spring Bootの基本的な構造（Controller / Service / Repository）と  
CRUD処理の流れを理解しました。