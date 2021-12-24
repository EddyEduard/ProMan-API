package com.team.proman.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern.Flag;

public class LoginModel {
	@NotBlank(message = "The email is required.")
	@NotNull(message = "The email cannot be null.")
	@NotEmpty(message = "The email cannot be empty.")
	@Email(message = "The email address is invalid.", flags = { Flag.CASE_INSENSITIVE })
	public String email;

	@NotBlank(message = "The password is required.")
	@NotNull(message = "The password cannot be null.")
	@NotEmpty(message = "The password cannot be empty.")
	@Size(min = 6, max = 50, message = "The length of password must be between 6 and 50 characters.")
	public String password;

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
}
