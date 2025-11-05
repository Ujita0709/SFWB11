package com.example.app.controller;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.app.domain.Item;
import com.example.app.mapper.FileMapper;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class IndexController {
	
	private static final String UPLOAD_DIRECTORY = "C:/Users/zdis58/gallery";
	private final FileMapper mapper;
	
	@GetMapping("/")
	public String showList(Model model) {
		File uploadsDirectory = new File(UPLOAD_DIRECTORY);
		List<Item> itemList = mapper.selectAll();
		model.addAttribute("items", itemList);
		
		return "index";
	}
	
	

}
