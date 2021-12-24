package com.team.proman.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
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
	public String username;

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

	@NotBlank(message = "The country is required.")
	@NotNull(message = "The country cannot be null.")
	@NotEmpty(message = "The country cannot be empty.")
	public String country;

	@NotBlank(message = "The industry is required.")
	@NotNull(message = "The industry cannot be null.")
	@NotEmpty(message = "The industry cannot be empty.")
	public String industry;

	@NotBlank(message = "The company is required.")
	@NotNull(message = "The company cannot be null.")
	@NotEmpty(message = "The company cannot be empty.")
	public String company;

	@Size(min = 9, max = 12, message = "The length of phone number must be between 9 and 12 digits.")
	public String phone;

	@Min(1)
	public Integer team_size;

	public Account getAccount() {
		return new Account(this.username, this.email, this.password, this.country, this.industry, this.company,
				this.phone, this.team_size);
	}
}
