package com.team.proman.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.team.proman.model.db.Project;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

}
