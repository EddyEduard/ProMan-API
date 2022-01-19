package com.team.proman.model;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern.Flag;
import javax.validation.constraints.Size;

import com.team.proman.model.db.Account;

public class AccountModel {
	@NotBlank(message = "The username is required.")
	@NotNull(message = "The username cannot be null.")
	@NotEmpty(message = "The username cannot be empty.")
	@Size(min = 6, max = 50, message = "The length of username must be between 6 and 50 characters.")
	private String username;

	@NotBlank(message = "The email is required.")
	@NotNull(message = "The email cannot be null.")
	@NotEmpty(message = "The email cannot be empty.")
	@Email(message = "The email address is invalid.", flags = { Flag.CASE_INSENSITIVE })
	private String email;

	@NotBlank(message = "The password is required.")
	@NotNull(message = "The password cannot be null.")
	@NotEmpty(message = "The password cannot be empty.")
	@Size(min = 6, max = 70, message = "The length of password must be between 6 and 70 characters.")
	private String password;

	@Size(min = 9, max = 11, message = "The length of phone number must be between 9 and 11 digits.")
	private String phone;
	
	private List<String> roles;

	public Account getAccount(Long companyId) {
		return new Account(companyId, this.username, this.email, this.password, this.phone);
	}

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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
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
}
