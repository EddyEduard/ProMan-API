package com.team.proman.model;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegisterModel {
	@NotBlank(message = "The username is required.")
	@NotNull(message = "The username cannot be null.")
	@NotEmpty(message = "The username cannot be empty.")
	@Size(min = 6, max = 50, message = "The length of username must be between 6 and 50 characters.")
	private String username;

	@NotBlank(message = "The password is required.")
	@NotNull(message = "The password cannot be null.")
	@NotEmpty(message = "The password cannot be empty.")
	@Size(min = 6, max = 70, message = "The length of password must be between 6 and 70 characters.")
	private String password;
	
	private List<String> roles;
	
	private CompanyModel company;
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the roles
	 */
	public List<String> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(List<String> roles) {
		this.roles = roles;
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
