package com.enotes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import com.enotes.dto.FavouriteNotesDto;
import com.enotes.dto.NotesDto;
import com.enotes.dto.NotesResponse;
import com.enotes.entity.FileDetails;
import com.enotes.service.NotesService;
import com.enotes.util.commonUtil;

@RestController
@RequestMapping("/api/v1/notes")
public class notesController {

    private final CategoryController categoryController;
	
	@Autowired
	private NotesService notesService;

    notesController(CategoryController categoryController) {
        this.categoryController = categoryController;
    }

	@PostMapping("/save")
	public ResponseEntity<?> saveNotes(@RequestParam String notesDto,@RequestParam(required = false) MultipartFile file)throws Exception{
		
		
		boolean saveNotes = notesService.saveNotes(notesDto,file);
		if(saveNotes) {
			return commonUtil.createBuildResponseMessage("Save Success",HttpStatus.CREATED);
		}
		return commonUtil.createErrorResponseMessage("Data Note Save",HttpStatus.INTERNAL_SERVER_ERROR);	
	}
	
	
	@GetMapping("/downloadFile/{id}")
	public ResponseEntity<?> downlodFile(@PathVariable Integer id) throws Exception{
	FileDetails fileDetails=notesService.getFileDetails(id);
	
	byte[] data=notesService.downloadFile(fileDetails);
	
	HttpHeaders headers=new HttpHeaders();
	String contentType= commonUtil.getContentType(fileDetails.getOriginalFileName());
	headers.setContentType(MediaType.parseMediaType(contentType));
	headers.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());
		
		return ResponseEntity.ok().headers(headers).body(data);
	}
	
	
	@GetMapping("/userNotesByPagination")
	public ResponseEntity<?>getUserNotesByPagination(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		Integer userId = 2;
		NotesResponse notes = notesService.getAllNotesByUser(userId,pageNo,pageSize);
//		if (CollectionUtils.isEmpty(notes)) {
//			return ResponseEntity.noContent().build();
//		}
		return commonUtil.createBuildResponse(notes, HttpStatus.OK);
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
	
	
	@GetMapping("/delete/{id}")
	public ResponseEntity<?> sofDeleteUserNotes(@PathVariable Integer id) throws Exception{
		
		boolean deletedNote =notesService.softDeleteNotes(id);
		if(deletedNote) {
			return commonUtil.createBuildResponseMessage("Delete Success", HttpStatus.OK);	
		}
		return  commonUtil.createErrorResponseMessage("Id Not Deleted ! Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@GetMapping("/restore/{id}")
	public ResponseEntity<?> restoreUserNotes(@PathVariable Integer id) throws Exception{
		
		boolean restoreNote =notesService.restoreNotes(id);
		if(restoreNote) {
			return commonUtil.createBuildResponseMessage("Restore Success", HttpStatus.OK);	
		}
		return  commonUtil.createErrorResponseMessage("Id Not Restore ! Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@GetMapping("/recycleBin")
	public ResponseEntity<?> getUserRecycleBinNotes() throws Exception{
		Integer userId=2;
		List<NotesDto> notesDto=notesService.getUserRecycleBinNotes(userId);
    	if(!CollectionUtils.isEmpty(notesDto)) {
			return commonUtil.createBuildResponse(notesDto, HttpStatus.OK);	
		}
		return  commonUtil.createErrorResponseMessage("Notes not avaible in Recycle Bin", HttpStatus.OK);	
	}
	
	@DeleteMapping("/forceDelete/{id}")
	public ResponseEntity<?> hardDeleteNote(@PathVariable Integer id) throws Exception{	
		notesService.hardDeleteNotes(id);
		return commonUtil.createBuildResponseMessage("Delete Success", HttpStatus.OK);
	}
	
	
	@DeleteMapping("/deleteAllBin")
	public ResponseEntity<?> emptyRecyleBin() throws Exception {
		int userId=2;
		notesService.emptyRecycleBin(userId);
		return commonUtil.createBuildResponseMessage("Delete Success", HttpStatus.OK);
	}
	
	
	@GetMapping("/fav-note/{id}")
	public ResponseEntity<?> setFavouriteNote(@PathVariable Integer id) throws Exception {
		int userId=2;
		boolean favNote=notesService.setFavouriteNote(id,userId);
		if(favNote) {
			return commonUtil.createBuildResponseMessage("Note Set As Favaourite", HttpStatus.OK);
		}
		return commonUtil.createErrorResponseMessage(" ! Note Not Set As Favaourite , Something Went Wrong.", HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@DeleteMapping("/unFav-note/{favNotId}")
	public ResponseEntity<?> unFavoriteNote(@PathVariable Integer favNotId) throws Exception {
		notesService.unFavoriteNotes(favNotId);
		return commonUtil.createBuildResponseMessage("Remove Favorite note", HttpStatus.OK);
	}

	@GetMapping("/allFav-notes")
	public ResponseEntity<?> getAllUserfavoriteNotes() throws Exception {

		List<FavouriteNotesDto> userFavoriteNotes = notesService.getUserFavoriteNotes();
		if (CollectionUtils.isEmpty(userFavoriteNotes)) {
			return ResponseEntity.noContent().build();
		}
		return commonUtil.createBuildResponse(userFavoriteNotes, HttpStatus.OK);
	}
	
	
		
}
