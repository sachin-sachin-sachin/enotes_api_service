package com.enotes.service_impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enotes.dto.NotesDto;
import com.enotes.dto.NotesDto.CategoryDto;
import com.enotes.entity.Notes;
import com.enotes.exception.ResourceNotFoundException;
import com.enotes.repository.CategoryRepo;
import com.enotes.repository.NotesRepo;
import com.enotes.service.NotesService;

@Service
public class notesServiceImpl implements NotesService{
	
	@Autowired
	private ModelMapper mapper;

	@Autowired
	private NotesRepo noteRepo;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Override
	public boolean saveNotes(NotesDto notesDto) throws Exception {
		
		//       category Validation ---------------
		checkCategoryExist(notesDto.getCategory());
		
		Notes map = mapper.map(notesDto, Notes.class);
		Notes save = noteRepo.save(map);
		
		if(!ObjectUtils.isEmpty(save)) {
		return true;	
		}
		return false;
	}
	
	private void checkCategoryExist(CategoryDto category) throws Exception {
		categoryRepo.findById(category.getId()).orElseThrow(() -> new ResourceNotFoundException("category id invalid"));
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
