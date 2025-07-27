package com.enotes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enotes.dto.NotesDto;
import com.enotes.service.NotesService;
import com.enotes.util.commonUtil;

@RestController
@RequestMapping("/api/v1/notes")
public class notesController {
	
	@Autowired
	private NotesService notesService;

	@PostMapping("/save")
	public ResponseEntity<?> saveNotes(@RequestBody NotesDto notesDto)throws Exception{
		boolean saveNotes = notesService.saveNotes(notesDto);
		if(saveNotes) {
			return commonUtil.createBuildResponseMessage("Save Success",HttpStatus.CREATED);
		}
		return commonUtil.createErrorResponseMessage("Data Note Save",HttpStatus.INTERNAL_SERVER_ERROR);	
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<?> getAllNotes(){
		List<NotesDto> allNotes = notesService.getAllNotes();
		if(!CollectionUtils.isEmpty(allNotes)) {
			return commonUtil.createBuildResponse(allNotes,HttpStatus.OK);
		}
		return ResponseEntity.noContent().build();
		// return commonUtil.createErrorResponseMessage("Data Note Save",HttpStatus.INTERNAL_SERVER_ERROR);	
	}
	
		
}
