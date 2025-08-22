package com.enotes.dto;

import com.enotes.entity.Category;
import com.enotes.entity.FileDetails;

import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavouriteNotesDto {
	
	private Integer id;

	private FavouriteNoteDtoResponse note;

	private Integer userId;
	
	
	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class FavouriteNoteDtoResponse {
		private Integer id;
		private String title;
		private String description;
		private CategoryDto category;
		private FileDetails fileDetails;
	}
	
	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class CategoryDto {
		private Integer id;
		private String name;
	}
	
	
	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class FileDetails {
		private Integer id;
		private String displayFileName;
		private String size;
	}
	
	
	
}
