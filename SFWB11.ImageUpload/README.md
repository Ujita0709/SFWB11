# プロジェクト概要
galleryテーブルから画像ファイル一覧を読み込み、画像をクリックしたら
詳細ページに遷移、詳細ページから編集ボタンで編集ページ、削除ボタンで
ファイルを削除して一覧ページに遷移、編集ページではファイル名の変更後
一覧ページに遷移する

## 画面構成
| URL | 画面名 | コントローラー | メソッド | GET/POST |
|---|---|---|---|---|
| / | 画像リスト | IndexController | showList | GET |
| /upload| 詳細 | UpdateController | showItem | GET |
| /edit | 編集 | UpdateController | editItem | POST|

## データ設計

 galleryテーブル

| column | データ型 | 備考
|---|---|---|
| id | INT | 番号 |
| name | varchar | ファイルの説明 |
| img_src | varchar | ファイル名 |
| memo | varchar | メモ |

