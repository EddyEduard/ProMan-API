package com.team.proman.model.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.team.proman.model.enums.Priority;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Table(name = "sprint")
@Entity
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class Sprint {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "INT")
	private Long company_id;
	@Column(columnDefinition = "INT")
	private Long project_id;
	@Column(columnDefinition = "VARCHAR", length = 250, nullable = false)
	private String name;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String description;

	@Enumerated(EnumType.STRING)
	private Priority priority;

	@Column(columnDefinition = "DATETIME", nullable = false)
	private Date created_date;

	@Column(columnDefinition = "DATETIME", nullable = false)
	private Date updated_date;

	/**
	 * @param company_id
	 * @param project_id
	 * @param name
	 * @param description
	 * @param priority
	 * @param created_date
	 * @param updated_date
	 */
	public Sprint(Long company_id, Long project_id, String name, String description, Priority priority,
			Date created_date, Date updated_date) {
		this.company_id = company_id;
		this.project_id = project_id;
		this.name = name;
		this.description = description;
		this.priority = priority;
		this.created_date = created_date;
		this.updated_date = updated_date;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the company_id
	 */
	public Long getCompany_id() {
		return company_id;
	}

	/**
	 * @param company_id the company_id to set
	 */
	public void setCompany_id(Long company_id) {
		this.company_id = company_id;
	}

	/**
	 * @return the project_id
	 */
	public Long getProject_id() {
		return project_id;
	}

	/**
	 * @param project_id the project_id to set
	 */
	public void setProject_id(Long project_id) {
		this.project_id = project_id;
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

	/**
	 * @return the created_date
	 */
	public Date getCreated_date() {
		return created_date;
	}

	/**
	 * @param created_date the created_date to set
	 */
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	/**
	 * @return the updated_date
	 */
	public Date getUpdated_date() {
		return updated_date;
	}

	/**
	 * @param updated_date the updated_date to set
	 */
	public void setUpdated_date(Date updated_date) {
		this.updated_date = updated_date;
	}
}
