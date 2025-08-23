package com.enotes.controllerEndpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.enotes.dto.TodoDto;
import static com.enotes.util.Constants.ROLE_USER;

@RequestMapping("/api/v1/todo")
public interface TodoEndpoint {
	
	@PostMapping("/")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> saveTodo(@RequestBody TodoDto todo) throws Exception ;
	
	@GetMapping("/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getTodoById(@PathVariable Integer id) throws Exception;
	
	@GetMapping("/list")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getAllTodoByUser() throws Exception;
	
}
