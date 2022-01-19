package com.team.proman.model;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.team.proman.model.db.Sprint;
import com.team.proman.model.enums.Priority;

public class SprintModel {
	private Long projectId;

	@NotBlank(message = "The name is required.")
	@NotNull(message = "The name cannot be null.")
	@NotEmpty(message = "The name cannot be empty.")
	@Size(min = 6, max = 250, message = "The length of name must be between 6 and 250 characters.")
	private String name;

	private String description;

	private Priority priority;

	public Sprint getSprint(Long companyId, Date created_date, Date updated_date) {
		return new Sprint(companyId, this.projectId, this.name, this.description, this.priority, created_date,
				updated_date);
	}

	/**
	 * @return the projectId
	 */
	public Long getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
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
	 * @return the priority
	 */
	public Priority getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(Priority priority) {
		this.priority = priority;
	}
}
