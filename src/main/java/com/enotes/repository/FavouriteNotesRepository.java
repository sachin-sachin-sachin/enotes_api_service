package com.enotes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enotes.entity.FavouriteNotes;

public interface FavouriteNotesRepository extends JpaRepository<FavouriteNotes,Integer> {

	List<FavouriteNotes> findByUserId(int userId);

//	boolean existsByUserId(int userId);

//	boolean existsByUserIdAndNoteId(int userId, Integer id);

//	boolean existsByNoteId(int userId, Integer id);

	boolean existsByNoteId(Integer id);

}
