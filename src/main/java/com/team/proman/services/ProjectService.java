package com.team.proman.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.team.proman.model.db.Project;
import com.team.proman.repositories.ProjectRepository;

@Service("projectService")
public class ProjectService {
	private final ProjectRepository projectRepository;

	/**
	 * @param projectRepository
	 */
	public ProjectService(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}

	/**
	 * Select projects by company id.
	 * 
	 * @param companyId
	 * @return selected projects
	 */
	public List<Project> selectByCompanyId(Long companyId) {
		List<Project> selectProjects = new ArrayList<Project>();
		Iterator<Project> projects = this.projectRepository.findAll().iterator();

		while (projects.hasNext()) {
			Project project = projects.next();
			if (project.getCompany_id().equals(companyId)) {
				selectProjects.add(project);
			}
		}

		return selectProjects;
	}
	
	/**
	 * Find a project by id.
	 * 
	 * @param id
	 * @return found project
	 */
	public Project findById(Long id) {
		return projectRepository.findById(id).get();
	}

	/**
	 * Create a new project.
	 * 
	 * @param project
	 * @return created project
	 */
	public Project create(Project project) {
		return this.projectRepository.save(project);
	}
	
	/**
	 * Update a project by id.
	 * 
	 * @param id
	 * @param project
	 * @return updated project
	 */
	public Project update(Long id, Project project) {
		Project foundProject = findById(id);
		foundProject.setName(project.getName());
		foundProject.setDescription(project.getDescription());
		foundProject.setStatus(project.getStatus());
		foundProject.setCreated_date(project.getCreated_date());
		foundProject.setUpdated_date(project.getUpdated_date());
		
		return projectRepository.save(foundProject);
	}
	
	/**
	 * Delete a project by id.
	 * 
	 * @param id
	 */
	public void deleteById(Long id) {
		Iterator<Project> projects = projectRepository.findAll().iterator();

		while (projects.hasNext()) {
			Project project = projects.next();
			if (project.getId().equals(id)) {
				projectRepository.delete(project);
			}
		}
	}
}
