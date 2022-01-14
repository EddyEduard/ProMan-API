package com.team.proman.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.team.proman.model.db.Task;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {

}
