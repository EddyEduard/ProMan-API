package com.team.proman.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.team.proman.model.db.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

}
