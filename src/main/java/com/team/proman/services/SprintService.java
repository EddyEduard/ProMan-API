package com.team.proman.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.team.proman.model.db.Sprint;
import com.team.proman.repositories.SprintRepository;

@Service("sprintService")
public class SprintService {
	private final SprintRepository sprintRepository;

	/**
	 * @param sprintRepository
	 */
	public SprintService(SprintRepository sprintRepository) {
		this.sprintRepository = sprintRepository;
	}

	/**
	 * Select sprints by project id.
	 * 
	 * @param projectId
	 * @return selected sprints
	 */
	public List<Sprint> selectByProjectId(Long projectId) {
		List<Sprint> selectSprints = new ArrayList<Sprint>();
		Iterator<Sprint> sprints = this.sprintRepository.findAll().iterator();

		while (sprints.hasNext()) {
			Sprint sprint = sprints.next();
			if (sprint.getProject_id().equals(projectId)) {
				selectSprints.add(sprint);
			}
		}

		return selectSprints;
	}
	
	/**
	 * Find a sprint by project id and his id.
	 * 
	 * @param projectId
	 * @param sprintId
	 * @return selected sprints
	 */
	public Sprint findByProjectIdAndSprintId(Long projectId, Long sprintId) {
		Iterator<Sprint> sprints = this.sprintRepository.findAll().iterator();

		while (sprints.hasNext()) {
			Sprint sprint = sprints.next();
			if (sprint.getProject_id().equals(projectId) && sprint.getId().equals(sprintId)) {
				return sprint;
			}
		}

		return null;
	}

	/**
	 * Find a sprint by id.
	 * 
	 * @param id
	 * @return found sprint
	 */
	public Sprint findById(Long id) {
		return sprintRepository.findById(id).get();
	}

	/**
	 * Create a new sprint.
	 * 
	 * @param sprint
	 * @return created sprint
	 */
	public Sprint create(Sprint sprint) {
		return this.sprintRepository.save(sprint);
	}

	/**
	 * Update a sprint by id.
	 * 
	 * @param id
	 * @param sprint
	 * @return updated sprint
	 */
	public Sprint update(Long id, Sprint sprint) {
		Sprint foundSprint = findById(id);
		foundSprint.setName(sprint.getName());
		foundSprint.setDescription(sprint.getDescription());
		foundSprint.setPriority(sprint.getPriority());
		foundSprint.setStatus(sprint.getStatus());
		foundSprint.setCreated_date(sprint.getCreated_date());
		foundSprint.setUpdated_date(sprint.getUpdated_date());

		return sprintRepository.save(foundSprint);
	}

	/**
	 * Delete a sprint by id.
	 * 
	 * @param id
	 */
	public void deleteById(Long id) {
		Iterator<Sprint> sprints = sprintRepository.findAll().iterator();

		while (sprints.hasNext()) {
			Sprint sprint = sprints.next();
			if (sprint.getId().equals(id)) {
				sprintRepository.delete(sprint);
			}
		}
	}
}
