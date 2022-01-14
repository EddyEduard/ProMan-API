package com.team.proman.services;

import java.util.Iterator;

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
	 * Find a company by id.
	 * 
	 * @param id
	 * @return found company or null
	 */
	public Company findById(Long id) {
		Iterator<Company> companies = companyRepository.findAll().iterator();

		while (companies.hasNext()) {
			Company company = companies.next();
			if (company.getId().equals(id)) {
				return company;
			}
		}

		return null;
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
	
	/**
	 * Update a company by id.
	 * 
	 * @param id
	 * @param account
	 * @return updated account
	 */
	public Company update(Long id, Company company) {
		Company foundCompany = findById(id);
		foundCompany.setName(company.getName());
		foundCompany.setEmail(company.getEmail());
		foundCompany.setPhone(company.getPhone());
		foundCompany.setIndustry(company.getIndustry());
		foundCompany.setCountry(company.getCountry());
		foundCompany.setCity(company.getCity());
		foundCompany.setAddress(company.getAddress());

		return companyRepository.save(foundCompany);
	}
	
	/**
	 * Delete a company profile.
	 * 
	 * @param company
	 */
	public void delete(Company company) {
		companyRepository.delete(company);
	}
}
