package com.team.proman.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RegisterModel {
	@NotBlank(message = "The account is required.")
	@NotNull(message = "The account cannot be null.")
	@NotEmpty(message = "The account cannot be empty.")
	private AccountModel account;
	
	@NotBlank(message = "The company is required.")
	@NotNull(message = "The company cannot be null.")
	@NotEmpty(message = "The company cannot be empty.")
	private CompanyModel company;

	/**
	 * @return the account
	 */
	public AccountModel getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(AccountModel account) {
		this.account = account;
	}

	/**
	 * @return the company
	 */
	public CompanyModel getCompany() {
		return company;
	}

	/**
	 * @param company the company to set
	 */
	public void setCompany(CompanyModel company) {
		this.company = company;
	}
}
