package com.enotes.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.enotes.dto.FavouriteNotesDto;
import com.enotes.dto.NotesDto;
import com.enotes.dto.NotesResponse;
import com.enotes.entity.FileDetails;

public interface NotesService {

	public boolean saveNotes(String notesDto, MultipartFile file)throws Exception;
	
	public List<NotesDto> getAllNotes();

	public FileDetails getFileDetails(Integer id) throws Exception;

	public byte[] downloadFile(FileDetails fileDetails) throws Exception;

	public NotesResponse getAllNotesByUser(Integer pageNo, Integer pageSize);

	public boolean softDeleteNotes(Integer id) throws Exception;

	public boolean restoreNotes(Integer id) throws Exception;

	public List<NotesDto> getUserRecycleBinNotes();

	public void hardDeleteNotes(Integer id)throws Exception;

	public void emptyRecycleBin()throws Exception;

	public boolean setFavouriteNote(Integer id) throws Exception;

	public void unFavoriteNotes(Integer favNotId)throws Exception;

	public List<FavouriteNotesDto> getUserFavoriteNotes()throws Exception;

	public Boolean copyNotes(Integer id)throws Exception;

	public NotesResponse getNotesByUserSearch(Integer pageNo, Integer pageSize,String keyword);
	}
