package com.team.proman.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.team.proman.model.db.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

}
