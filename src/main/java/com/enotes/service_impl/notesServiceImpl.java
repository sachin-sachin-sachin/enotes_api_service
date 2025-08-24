package com.enotes.service_impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.enotes.dto.FavouriteNotesDto;
import com.enotes.dto.NotesDto;
import com.enotes.dto.NotesDto.CategoryDto;
import com.enotes.dto.NotesDto.FilesDto;
import com.enotes.dto.NotesResponse;
import com.enotes.entity.FavouriteNotes;
import com.enotes.entity.FileDetails;
import com.enotes.entity.Notes;
import com.enotes.exception.AlreadyFavoritedException;
import com.enotes.exception.ResourceNotFoundException;
import com.enotes.repository.CategoryRepo;
import com.enotes.repository.FavouriteNotesRepository;
import com.enotes.repository.FileRepository;
import com.enotes.repository.NotesRepo;
import com.enotes.service.NotesService;
import com.enotes.util.commonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class notesServiceImpl implements NotesService{
	
	@Autowired
	private ModelMapper mapper;

	@Autowired
	private NotesRepo noteRepo;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private FileRepository fileRepo;
	
	@Autowired
	private FavouriteNotesRepository favNoteRepo;
	
	@Value("${file.upload.path}")
	private String uploadPath;
	
	@Override
	public boolean saveNotes(String notesDto,MultipartFile file) throws Exception {
		
		ObjectMapper obMapper=new ObjectMapper();
		NotesDto notesDtoObject = obMapper.readValue(notesDto,NotesDto.class);
		
		notesDtoObject.setIsDeleted(false);
		notesDtoObject.setDeletedOn(null);
		
		// update notes if id is given in request
				if (!ObjectUtils.isEmpty(notesDtoObject.getId())) {
					updateNotes(notesDtoObject, file);
				}
			
		//       category Validation ---------------
		checkCategoryExist(notesDtoObject.getCategory());
		
		Notes notesMap = mapper.map(notesDtoObject, Notes.class);
		
	FileDetails fileDetails= saveFileDetails(file);
		
		if(!ObjectUtils.isEmpty(fileDetails)) {
			notesMap.setFileDetails(fileDetails);
		}else {
			if (ObjectUtils.isEmpty(notesDtoObject.getId())) {
				notesMap.setFileDetails(null);
			}
		}
		
		Notes save = noteRepo.save(notesMap);
		
		if(!ObjectUtils.isEmpty(save)) {
		return true;	
		}
		return false;
	}
	
	private void updateNotes(NotesDto notesDtoObject, MultipartFile file) throws Exception {
		Notes existNotes = noteRepo.findById(notesDtoObject.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Invalid Notes id"));

		// user not choose any file at update time
		if (ObjectUtils.isEmpty(file)) {
			notesDtoObject.setFileDetails(mapper.map(existNotes.getFileDetails(), FilesDto.class));
		}
		
	}

	private FileDetails saveFileDetails(MultipartFile file) throws IOException {
		
		if(!ObjectUtils.isEmpty(file) &&!file.isEmpty()) {
		
		String originalFilename = file.getOriginalFilename();
		
		String extension = FilenameUtils.getExtension(originalFilename);
		
		List<String> extentionVarify = Arrays.asList("pdf","png","jpg");
		if(!extentionVarify.contains(extension)) {
			throw new IllegalArgumentException("Invalid File Format ! Ipload Only .pdf , .png ,.jpeg Files");
		}
		String RandomString=UUID.randomUUID().toString();
		String uploadFileName= RandomString+"."+extension;
		
		long fileSize=file.getSize();
		
		String displayFileName= getDisplayName(originalFilename);
		
		File saveFile=new File(uploadPath);
		if(!saveFile.exists()) {
			saveFile.mkdir();
		}
		
		String storePath=uploadPath.concat(uploadFileName);
		
		long upload = Files.copy(file.getInputStream(),Paths.get(storePath));
		
		if(upload!=0) {
			FileDetails fileDetails=new FileDetails();
			fileDetails.setOriginalFileName(originalFilename);
			fileDetails.setUploadFileName(uploadFileName);
			fileDetails.setDisplayFileName(displayFileName);
			fileDetails.setPath(storePath);
			fileDetails.setFileSize(fileSize);
			FileDetails save = fileRepo.save(fileDetails);
			return save;
		}
		}
		return null;
	}

	private String getDisplayName(String originalFilename) {
		String extension = FilenameUtils.getExtension(originalFilename);
		String fileNameWithoutExtension=FilenameUtils.removeExtension(originalFilename);
		
		if(fileNameWithoutExtension.length()>8) {
			fileNameWithoutExtension=fileNameWithoutExtension.substring(0,7);
		}
		
		fileNameWithoutExtension=fileNameWithoutExtension+"."+extension;
		return fileNameWithoutExtension;
	}

	private void checkCategoryExist(CategoryDto category) throws Exception {
		categoryRepo.findById(category.getId()).orElseThrow(() -> new ResourceNotFoundException("category id invalid"));
	}

	
	
	
	@Override
	public FileDetails getFileDetails(Integer id) throws Exception {
		FileDetails fileDetails = fileRepo.findById(id).orElseThrow(()->new ResourceNotFoundException(""));
		return fileDetails;
	}

	@Override
	public byte[] downloadFile(FileDetails fileDetails) throws Exception {
		InputStream io=new FileInputStream(fileDetails.getPath());
		return StreamUtils.copyToByteArray(io);
	}
	
	
	

	@Override
	public List<NotesDto> getAllNotes() { 
		   //  three Line Code ---------------------
//		List<notes> all = noteRepo.findAll();
//		List<notesDto> list = all.stream().map(note->mapper.map(note, notesDto.class)).toList();
//		return list;
				
	// 	One Line Code  ---------------
	 return noteRepo.findAll().stream().map(note -> mapper.map(note, NotesDto.class)).toList();
	}
	
	
	

	@Override
	public NotesResponse getAllNotesByUser(Integer pageNo, Integer pageSize) {
		Integer userId = commonUtil.getLoggedInUser().getId();
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<Notes> pageNotes = noteRepo.findByCreatedBy(userId, pageable);

		List<NotesDto> notesDto = pageNotes.get().map(n -> mapper.map(n, NotesDto.class)).toList();

		NotesResponse notes = NotesResponse.builder().notes(notesDto).pageNo(pageNotes.getNumber())
				.pageSize(pageNotes.getSize()).totalElements(pageNotes.getTotalElements())
				.totalPages(pageNotes.getTotalPages()).isFirst(pageNotes.isFirst()).isLast(pageNotes.isLast()).build();

		return notes;
	}
	
	
	@Override
	public boolean softDeleteNotes(Integer id) throws Exception {

		Notes notes = noteRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Notes id invalid ! Not Found"));
		notes.setIsDeleted(true);
		notes.setDeletedOn(LocalDateTime.now());
		Notes noteData=noteRepo.save(notes);
		if(!ObjectUtils.isEmpty(noteData)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean restoreNotes(Integer id) throws Exception {
		Notes notes = noteRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Notes id invalid ! Not Found"));
		notes.setIsDeleted(false);
		notes.setDeletedOn(null);
		Notes save = noteRepo.save(notes);
		if(!ObjectUtils.isEmpty(save)) {
			return true;
		}
		return false;
	}

	@Override
	public List<NotesDto> getUserRecycleBinNotes() {
		Integer userId = commonUtil.getLoggedInUser().getId();
		List<Notes> recycleBinNotes= noteRepo.findByCreatedByAndIsDeletedTrue(userId);
		List<NotesDto> notesDtoList = recycleBinNotes.stream().map(note->mapper.map(note,NotesDto.class)).toList();	
		return notesDtoList;
	}

	@Override
	public void hardDeleteNotes(Integer id) throws Exception {
	Notes notes = noteRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notes not found"));
	if (notes.getIsDeleted()) {
		noteRepo.delete(notes);
	} else {
		throw new IllegalArgumentException("Sorry You cant hard delete Directly");
	}
}
	

	@Override
	public void emptyRecycleBin() throws Exception {
		Integer userId = commonUtil.getLoggedInUser().getId();
		List<Notes> recycleNotes = noteRepo.findByCreatedByAndIsDeletedTrue(userId);
		if (!CollectionUtils.isEmpty(recycleNotes)) {
			noteRepo.deleteAll(recycleNotes);
		}
	}

	@Override
	public boolean setFavouriteNote(Integer id) throws Exception {
		Integer userId = commonUtil.getLoggedInUser().getId();
		 // Check if already favorited
	    if (favNoteRepo.existsByNoteId(id)) {
	        throw new AlreadyFavoritedException("Note already marked as favorite.");
	    }
		Notes note = noteRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Id Not Found"));
		FavouriteNotes favNote=FavouriteNotes.builder()
				.note(note)
				.userId(userId)
				.build();
		FavouriteNotes save = favNoteRepo.save(favNote);
		if(!ObjectUtils.isEmpty(save)){
			return true;
		}
		return false;
	}
	
	@Override
	public void unFavoriteNotes(Integer favouriteNoteId) throws Exception {
		FavouriteNotes favNote = favNoteRepo.findById(favouriteNoteId)
				.orElseThrow(() -> new ResourceNotFoundException("Favourite Note Not found & Id invalid"));
		favNoteRepo.delete(favNote);
	}

	@Override
	public List<FavouriteNotesDto> getUserFavoriteNotes() throws Exception {
		Integer userId = commonUtil.getLoggedInUser().getId();
		List<FavouriteNotes> favouriteNotes = favNoteRepo.findByUserId(userId);
		return favouriteNotes.stream().map(fn -> mapper.map(fn, FavouriteNotesDto.class)).toList();
	}	
	
	
	@Override
	public Boolean copyNotes(Integer id) throws Exception {
		Notes notes = noteRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Notes id invalid ! Not Found"));

		Notes copyNote = Notes.builder().title(notes.getTitle()).description(notes.getDescription())
				.category(notes.getCategory()).isDeleted(false).fileDetails(null).build();
		
		// TODO : Need to check User Validation
		Notes saveCopyNote = noteRepo.save(copyNote);
		if (!ObjectUtils.isEmpty(saveCopyNote)) {
			return true;
		}
		return false;
	}
		
	
	@Override
	public NotesResponse getNotesByUserSearch(Integer pageNo, Integer pageSize,String keyword) {
		Integer userId = commonUtil.getLoggedInUser().getId();
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<Notes> pageNotes = noteRepo.searchNotes(keyword,userId, pageable);

		List<NotesDto> notesDto = pageNotes.get().map(n -> mapper.map(n, NotesDto.class)).toList();

		NotesResponse notes = NotesResponse.builder().notes(notesDto).pageNo(pageNotes.getNumber())
				.pageSize(pageNotes.getSize()).totalElements(pageNotes.getTotalElements())
				.totalPages(pageNotes.getTotalPages()).isFirst(pageNotes.isFirst()).isLast(pageNotes.isLast()).build();

		return notes;
	}

}
