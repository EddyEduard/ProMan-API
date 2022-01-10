package com.team.proman.services;

import org.springframework.stereotype.Service;

import com.team.proman.model.db.Company;
import com.team.proman.repositories.CompanyRepository;

@Service("companyService")
public class CompanyService {
	
	private final CompanyRepository companyRepository;

	/**
	 * @param companyRepository
	 */
	public CompanyService(CompanyRepository companyRepository) {
		this.companyRepository = companyRepository;
	}
	
	/**
	 * Create a new company profile.
	 * 
	 * @param company
	 * @return created company
	 */
	public Company create(Company company) {
		return companyRepository.save(company);
	}
}
