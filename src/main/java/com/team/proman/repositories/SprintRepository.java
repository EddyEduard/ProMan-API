package com.team.proman.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.team.proman.model.db.Sprint;

@Repository
public interface SprintRepository extends CrudRepository<Sprint, Long> {

}
