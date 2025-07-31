package com.enotes.service;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.enotes.dto.NotesDto;
import com.enotes.dto.NotesResponse;
import com.enotes.entity.FileDetails;
import com.enotes.exception.ResourceNotFoundException;

public interface NotesService {

	public boolean saveNotes(String notesDto, MultipartFile file)throws Exception;
	
	public List<NotesDto> getAllNotes();

	public FileDetails getFileDetails(Integer id) throws Exception;

	public byte[] downloadFile(FileDetails fileDetails) throws Exception;

	public NotesResponse getAllNotesByUser(Integer userId, Integer pageNo, Integer pageSize);

	}
