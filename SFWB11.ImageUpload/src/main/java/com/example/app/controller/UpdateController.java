package com.example.app.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.app.domain.Item;
import com.example.app.mapper.FileMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UpdateController {

    //アップロード行うフォルダ
	private static final String UPLOAD_DIRECTORY = "C:/Users/zdis58/gallery";
    //マッパーオブジェクト
	private final FileMapper mapper;

	//localhost:8080/uploadにGETメソッドでアクセスがあったときに
	//以下のメソッドが実行される
    @GetMapping("/upload")
    public String uploadGet() {
    	//upload.htmlが返される
        return "upload";
    }

    //localhost:8080/uploadにPOSTメソッドでアクセスがあったときに
    //以下のメソッドが実行される
    @PostMapping("/upload")
    public String uploadPost(@RequestParam("upfile") MultipartFile upfile,
                             Model model) throws Exception {
        //アップロードファイルが空の場合ファイル一覧にリダイレクト
    	if (upfile.isEmpty()) {
    		//upload.htmlが返される
            return "redirect:/upload";
        }
    	//アップロードファイルが画像でない場合ファイル一覧にリダイレクト
        if (upfile.getContentType() == null || !upfile.getContentType().startsWith("image/")) {
        	//upload.htmlが返される
        	return "redirect:/upload";
        }

        //ファイル名
        String fileName = upfile.getOriginalFilename();
        //アップロード先のファイル
        File dest = new File(UPLOAD_DIRECTORY, fileName);
        //画像ファイルをアップロード先にコピーする
        upfile.transferTo(dest);

        // アップロードしたファイルをDBに登録
        Item item = new Item();
        item.setName(""); // 画像名
        item.setImgSrc(fileName);//ファイル名
        item.setMemo("");//メモ
        //insert文を実行する
        mapper.insert(item);

        //ファイル名を一時保存
        model.addAttribute("fileName", fileName);
        //updateDone.htmlを返す
        return "uploadDone";
    }

    // 詳細表示 
    //localhost:8080/detail/番号にGETメソッドでアクセスがあったときに
    //このメソッドが実行される
    @GetMapping("/detail/{id}")
    public String showItem(@PathVariable("id") Integer id, Model model) {
        //入力された番号でDBを検索
    	Item item = mapper.selectById(id);
        //アップロードファイルが空の場合ファイル一覧にリダイレクト
        if (item == null) {
            return "redirect:/";
        }
        //検索されたアップロードファイルを一時保存
        model.addAttribute("item", item);
        //detail.htmlを返す
        return "detail";
    }

    // 編集画面表示 
    //localhost:8080/edit/番号にGETメソッドでアクセスがあったときに
    //このメソッドが実行される
    @GetMapping("/edit/{id}")
    public String editItemGet(@PathVariable("id") Integer id, Model model) {
    	//入力された番号でDBを検索
    	Item item = mapper.selectById(id);
        
        //空の場合はファイル一覧にリダイレクト
        if (item == null) {
            return "redirect:/";
        }
        //itemオブジェクトをmodelに保存
        model.addAttribute("item", item);
        return "edit"; // edit.html を返す
    }

    //localhost:8080/edit/番号にGETメソッドでアクセスがあったときに
    //このメソッドが実行される
    @PostMapping("/edit/{id}")
    public String editItemPost(@PathVariable("id") Integer id,//番号
                               @RequestParam("name") String name,//画像名
                               @RequestParam(value = "memo", required = false) String memo,//画像名
                               @RequestParam(value = "newFileName", required = false) String newFileName) throws IOException {
                               //変更後のファイル名
    	
    	//入力された番号でDBを検索
    	Item item = mapper.selectById(id);
    	//空の場合はファイル一覧にリダイレクト
        if (item == null) {
            return "redirect:/";
        }

        // 画像ファイル名変更が指定されていればリネーム
        if (newFileName != null && !newFileName.isBlank() && !newFileName.equals(item.getImgSrc())) {
            Path oldPath = Paths.get(UPLOAD_DIRECTORY, item.getImgSrc());//旧ファイル名
            Path newPath = Paths.get(UPLOAD_DIRECTORY, newFileName);//変更後のファイル名
            Files.move(oldPath, newPath);//ファイル名の更新
            item.setImgSrc(newFileName);//ファイルの更新
        }

        //画像名の更新
        item.setName(name);
        //メモの更新
        item.setMemo(memo != null ? memo : "");
        //DBの画像ファイルを更新
        mapper.update(item);

        //detail.htmlと番号を返す
        return "redirect:/detail/" + id;
    }

    // 削除（DB行＋実ファイル）
    //localhost:8080/dell/番号にGETメソッドでアクセスがあったときに
    //このメソッドが実行される
    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable("id") Integer id) throws IOException {
        
    	//入力された番号でDBを検索
    	Item item = mapper.selectById(id);
    	//DBに指定した画像ファイルが登録されている場合
        if (item != null) {
        	//アップロードフォルダ内に指定したファイルが存在する場合
            Path path = Paths.get(UPLOAD_DIRECTORY, item.getImgSrc());
            if (Files.exists(path)) {
            	//ファイルを削除する
                Files.delete(path);
            }
            //DBの画像に関するデータを削除する
            mapper.deleteById(id);
        }
        //ファイル一覧にリダイレクトする
        return "redirect:/";
    }
}