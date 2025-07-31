package com.enotes.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enotes.entity.Notes;

public interface NotesRepo extends JpaRepository<Notes, Integer>{

	Page<Notes> findByCreatedBy(Integer userId, Pageable pageable);

	List<Notes> findByCreatedByAndIsDeletedTrue(Integer userId);

}
