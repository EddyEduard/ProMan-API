package com.team.proman.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.team.proman.model.db.Company;

import javax.validation.constraints.Pattern.Flag;

public class CompanyModel {
	@NotBlank(message = "The name is required.")
	@NotNull(message = "The name cannot be null.")
	@NotEmpty(message = "The name cannot be empty.")
	@Size(min = 6, max = 50, message = "The length of name must be between 6 and 50 characters.")
	private String name;

	@NotBlank(message = "The email is required.")
	@NotNull(message = "The email cannot be null.")
	@NotEmpty(message = "The email cannot be empty.")
	@Email(message = "The email address is invalid.", flags = { Flag.CASE_INSENSITIVE })
	private String email;

	@Size(min = 9, max = 12, message = "The length of phone number must be between 9 and 12 digits.")
	private String phone;

	@NotBlank(message = "The industry is required.")
	@NotNull(message = "The industry cannot be null.")
	@NotEmpty(message = "The industry cannot be empty.")
	private String industry;

	@NotBlank(message = "The country is required.")
	@NotNull(message = "The country cannot be null.")
	@NotEmpty(message = "The country cannot be empty.")
	private String country;

	@NotBlank(message = "The city is required.")
	@NotNull(message = "The city cannot be null.")
	@NotEmpty(message = "The city cannot be empty.")
	private String city;

	@NotBlank(message = "The address is required.")
	@NotNull(message = "The address cannot be null.")
	@NotEmpty(message = "The address cannot be empty.")
	private String address;

	public Company getCompany() {
		return new Company(this.name, this.email, this.phone, this.industry, this.country, this.city, this.address);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the industry
	 */
	public String getIndustry() {
		return industry;
	}

	/**
	 * @param industry the industry to set
	 */
	public void setIndustry(String industry) {
		this.industry = industry;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
}
