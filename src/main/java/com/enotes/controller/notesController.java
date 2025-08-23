package com.enotes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.enotes.controllerEndpoint.notesEndpoint;
import com.enotes.dto.FavouriteNotesDto;
import com.enotes.dto.NotesDto;
import com.enotes.dto.NotesResponse;
import com.enotes.entity.FileDetails;
import com.enotes.service.NotesService;
import com.enotes.util.commonUtil;

@RestController
public class notesController implements notesEndpoint{

    private final CategoryController categoryController;
	
	@Autowired
	private NotesService notesService;

    notesController(CategoryController categoryController) {
        this.categoryController = categoryController;
    }

	@Override
	public ResponseEntity<?> saveNotes(@RequestParam String notesDto,@RequestParam(required = false) MultipartFile file)throws Exception{
		
		
		boolean saveNotes = notesService.saveNotes(notesDto,file);
		if(saveNotes) {
			return commonUtil.createBuildResponseMessage("Save Success",HttpStatus.CREATED);
		}
		return commonUtil.createErrorResponseMessage("Data Note Save",HttpStatus.INTERNAL_SERVER_ERROR);	
	}
	
	
	@Override
	public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception{
	FileDetails fileDetails=notesService.getFileDetails(id);
	
	byte[] data=notesService.downloadFile(fileDetails);
	
	HttpHeaders headers=new HttpHeaders();
	String contentType= commonUtil.getContentType(fileDetails.getOriginalFileName());
	headers.setContentType(MediaType.parseMediaType(contentType));
	headers.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());
		
		return ResponseEntity.ok().headers(headers).body(data);
	}
	
	
	@Override
	public ResponseEntity<?>getAllUserNotesByPagination(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		Integer userId = 2;
		NotesResponse notes = notesService.getAllNotesByUser(pageNo,pageSize);
//		if (CollectionUtils.isEmpty(notes)) {
//			return ResponseEntity.noContent().build();
//		}
		return commonUtil.createBuildResponse(notes, HttpStatus.OK);
	}

	
	
	@Override
	public ResponseEntity<?> getAllNotes(){
		List<NotesDto> allNotes = notesService.getAllNotes();
		if(!CollectionUtils.isEmpty(allNotes)) {
			return commonUtil.createBuildResponse(allNotes,HttpStatus.OK);
		}
		return ResponseEntity.noContent().build();
		// return commonUtil.createErrorResponseMessage("Data Note Save",HttpStatus.INTERNAL_SERVER_ERROR);	
	}
	
	
	@Override
	public ResponseEntity<?> sofDeleteUserNotes(@PathVariable Integer id) throws Exception{
		
		boolean deletedNote =notesService.softDeleteNotes(id);
		if(deletedNote) {
			return commonUtil.createBuildResponseMessage("Delete Success", HttpStatus.OK);	
		}
		return  commonUtil.createErrorResponseMessage("Id Not Deleted ! Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@Override
	public ResponseEntity<?> restoreUserNotes(@PathVariable Integer id) throws Exception{
		
		boolean restoreNote =notesService.restoreNotes(id);
		if(restoreNote) {
			return commonUtil.createBuildResponseMessage("Restore Success", HttpStatus.OK);	
		}
		return  commonUtil.createErrorResponseMessage("Id Not Restore ! Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@Override
	public ResponseEntity<?> getUserRecycleBinNotes() throws Exception{
		List<NotesDto> notesDto=notesService.getUserRecycleBinNotes();
    	if(!CollectionUtils.isEmpty(notesDto)) {
			return commonUtil.createBuildResponse(notesDto, HttpStatus.OK);	
		}
		return  commonUtil.createErrorResponseMessage("Notes not avaible in Recycle Bin", HttpStatus.OK);	
	}
	
	@Override
	public ResponseEntity<?> hardDeleteNote(@PathVariable Integer id) throws Exception{	
		notesService.hardDeleteNotes(id);
		return commonUtil.createBuildResponseMessage("Delete Success", HttpStatus.OK);
	}
	
	
	@Override
	public ResponseEntity<?> emptyRecyleBin() throws Exception {
		notesService.emptyRecycleBin();
		return commonUtil.createBuildResponseMessage("Delete Success", HttpStatus.OK);
	}
	
	
	@Override
	public ResponseEntity<?> setFavouriteNote(@PathVariable Integer id) throws Exception {
		boolean favNote=notesService.setFavouriteNote(id);
		if(favNote) {
			return commonUtil.createBuildResponseMessage("Note Set As Favaourite", HttpStatus.OK);
		}
		return commonUtil.createErrorResponseMessage(" ! Note Not Set As Favaourite , Something Went Wrong.", HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@Override
	public ResponseEntity<?> unFavoriteNote(@PathVariable Integer favNotId) throws Exception {
		notesService.unFavoriteNotes(favNotId);
		return commonUtil.createBuildResponseMessage("Remove Favorite note", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getAllUserfavoriteNotes() throws Exception {

		List<FavouriteNotesDto> userFavoriteNotes = notesService.getUserFavoriteNotes();
		if (CollectionUtils.isEmpty(userFavoriteNotes)) {
			return ResponseEntity.noContent().build();
		}
		return commonUtil.createBuildResponse(userFavoriteNotes, HttpStatus.OK);
	}
	
	
	
	@Override
	public ResponseEntity<?> copyNotes(@PathVariable Integer id) throws Exception {
		Boolean copyNotes = notesService.copyNotes(id);
		if (copyNotes) {
			return commonUtil.createBuildResponseMessage("Copied success", HttpStatus.CREATED);
		}
		return commonUtil.createErrorResponseMessage("Copy Failed ! Try Again", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@Override
	public ResponseEntity<?> searchNotes(@RequestParam(name = "key",defaultValue = "") String key,
			@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		NotesResponse notes = notesService.getNotesByUserSearch(pageNo, pageSize,key);
		return commonUtil.createBuildResponse(notes, HttpStatus.OK);
	}
		
}
