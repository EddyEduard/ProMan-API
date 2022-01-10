package com.team.proman.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.team.proman.model.db.AccountRole;

@Repository
public interface AccountRoleRepository extends CrudRepository<AccountRole, Long> {

}
