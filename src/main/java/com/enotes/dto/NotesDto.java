package com.enotes.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotesDto {

private Integer id;
	
	private String title;
	
	private String description;
	
	private CategoryDto category;
	
	private Integer created_by;

	private Date created_on;

	private Integer updated_by;

	private Date updated_on;
	
	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class CategoryDto {
		private Integer id;
		private String name;
	}

}
