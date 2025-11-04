package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.Item;

@Mapper
public interface FileMapper {
	List<Item> selectAll();
	Item selectById(Integer id);
	void insert(Item item);
	void update(Item item);
	void deleteById(Integer id);

}
