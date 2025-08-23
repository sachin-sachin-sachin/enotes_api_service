package com.enotes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import org.springframework.web.bind.annotation.RestController;

import com.enotes.controllerEndpoint.TodoEndpoint;
import com.enotes.dto.TodoDto;
import com.enotes.service.TodoService;
import com.enotes.util.commonUtil;

@RestController
public class TodoController implements TodoEndpoint{

	@Autowired
	private TodoService todoService;

	@Override
	public ResponseEntity<?> saveTodo(TodoDto todo) throws Exception {
		Boolean saveTodo = todoService.saveTodo(todo);
		if (saveTodo) {
			return commonUtil.createBuildResponseMessage("Todo Saved Success", HttpStatus.CREATED);
		} else {
			return commonUtil.createErrorResponseMessage("Todo not save", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<?> getTodoById(Integer id) throws Exception {
		TodoDto todo = todoService.getTodoById(id);
		return commonUtil.createBuildResponse(todo, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getAllTodoByUser() throws Exception {
		List<TodoDto> todoList = todoService.getTodoByUser();
		if (CollectionUtils.isEmpty(todoList)) {
			return ResponseEntity.noContent().build();
		}
		return commonUtil.createBuildResponse(todoList, HttpStatus.OK);
	}
	
}
