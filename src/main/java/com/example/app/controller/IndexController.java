package com.example.app.controller;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class IndexController {
	
	private static final String UPLOAD_DIRECTORY = "C:/Users/zdis58/gallery";
	
	@GetMapping("/")
	public String inputFileName() {
		return "index";
	}
	
	@PostMapping("/")
	public String showGallery(Model model) {
		// アップロードされているファイルのリスト(表示用)の取得
		 File uploadsDirectory = new File(UPLOAD_DIRECTORY);
		 File[] fileList = uploadsDirectory.listFiles();
		 List<String> fileNames = Arrays.stream(fileList)
		 .map(file -> file.getName()).toList();
		 model.addAttribute("fileNames", fileNames); 
		
		return "redirect/upload";
	}

}
