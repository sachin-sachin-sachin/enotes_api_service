package com.enotes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enotes.entity.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer> {

}
