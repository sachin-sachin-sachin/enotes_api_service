package com.enotes.controllerEndpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/v1/notes")
public interface notesEndpoint {
	@PostMapping("/")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam(required = false) MultipartFile file)
			throws Exception ;
	
	@GetMapping("/download/{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception;
	
	@GetMapping("/")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllNotes();
	
	@GetMapping("/search")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> searchNotes(@RequestParam(name = "key",defaultValue = "") String key,
			@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize);
	
	@GetMapping("/user-notes")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getAllUserNotesByPagination(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize);
	
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> sofDeleteUserNotes(@PathVariable Integer id) throws Exception;
	
	@GetMapping("/restore/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> restoreUserNotes(@PathVariable Integer id) throws Exception ;
	
	@GetMapping("/recycle-bin")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getUserRecycleBinNotes() throws Exception;
	
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> hardDeleteNote(@PathVariable Integer id) throws Exception ;
	
	@DeleteMapping("/delete")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> emptyRecyleBin() throws Exception;
	
	@GetMapping("/fav/{noteId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> setFavouriteNote(@PathVariable Integer noteId) throws Exception ;
	
	@DeleteMapping("/un-fav/{favNotId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> unFavoriteNote(@PathVariable Integer favNotId) throws Exception;
	
	@GetMapping("/allFav-note")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getAllUserfavoriteNotes() throws Exception;
	
	@GetMapping("/copy/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> copyNotes(@PathVariable Integer id) throws Exception;
}
