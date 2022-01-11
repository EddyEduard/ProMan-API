package com.team.proman.model;

public class RegisterModel {
	private AccountModel account;
	
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
