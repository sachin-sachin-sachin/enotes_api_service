package com.enotes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enotes.entity.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer> {

	List<Category> findByIsActiveTrue();

	Optional<Category> findByIdAndIsActiveTrueAndIsDeletedFalse(Integer id);

	List<Category> findByIsDeletedFalse();

	List<Category> findByIsActiveTrueAndIsDeletedFalse();

}
