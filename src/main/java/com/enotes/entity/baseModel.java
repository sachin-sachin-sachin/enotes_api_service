package com.enotes.entity;

import java.util.Date;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class baseModel {

	private Boolean isActive;
	
	private Boolean isDeleted;
	
	private Integer created_by;
	
	private Date created_on;
	
	private Integer updated_by;
	
	private Date updated_on;
	
	
	
	
}
