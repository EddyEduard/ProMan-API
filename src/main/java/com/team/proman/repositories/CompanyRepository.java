package com.team.proman.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.team.proman.model.db.Company;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Long> {

}
