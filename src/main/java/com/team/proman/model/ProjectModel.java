package com.team.proman.model;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.team.proman.model.db.Project;
import com.team.proman.model.enums.Status;

public class ProjectModel {
	@NotBlank(message = "The name is required.")
	@NotNull(message = "The name cannot be null.")
	@NotEmpty(message = "The name cannot be empty.")
	@Size(min = 6, max = 50, message = "The length of name must be between 6 and 250 characters.")
	private String name;
	
	private String description;
	
	private Status status;
	
	public Project getProject(Long companyId, Status status, Date created_date, Date updated_date) {
		return new Project(companyId, this.name, this.description, status,  created_date, updated_date);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
}
