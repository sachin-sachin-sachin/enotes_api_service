package com.enotes.service_impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.enotes.dto.NotesDto;
import com.enotes.dto.NotesDto.CategoryDto;
import com.enotes.entity.FileDetails;
import com.enotes.entity.Notes;
import com.enotes.exception.ResourceNotFoundException;
import com.enotes.repository.CategoryRepo;
import com.enotes.repository.FileRepository;
import com.enotes.repository.NotesRepo;
import com.enotes.service.NotesService;
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
	
	@Value("${file.upload.path}")
	private String uploadPath;
	
	@Override
	public boolean saveNotes(String notesDto,MultipartFile file) throws Exception {
		
		ObjectMapper obMapper=new ObjectMapper();
		NotesDto notesDtoObject = obMapper.readValue(notesDto,NotesDto.class);
			
		//       category Validation ---------------
		checkCategoryExist(notesDtoObject.getCategory());
		
		Notes notesMap = mapper.map(notesDtoObject, Notes.class);
		
	FileDetails fileDetails= saveFileDetails(file);
		
		if(!ObjectUtils.isEmpty(fileDetails)) {
			notesMap.setFileDetails(fileDetails);
		}else {
			notesMap.setFileDetails(null);
		}
		
		Notes save = noteRepo.save(notesMap);
		
		if(!ObjectUtils.isEmpty(save)) {
		return true;	
		}
		return false;
	}
	
	private FileDetails saveFileDetails(MultipartFile file) throws IOException {
		
		if(!file.isEmpty()) {
		
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

}
