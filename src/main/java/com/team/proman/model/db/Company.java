package com.team.proman.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Table(name = "company")
@Entity
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class Company {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "VARCHAR", length = 250, nullable = false)
	private String name;

	@Column(columnDefinition = "VARCHAR", unique = true, nullable = false)
	private String email;

	@Column(columnDefinition = "VARCHAR", length = 11)
	private String phone;

	@Column(columnDefinition = "VARCHAR", length = 250, nullable = false)
	private String industry;

	@Column(columnDefinition = "VARCHAR", length = 250, nullable = false)
	private String country;

	@Column(columnDefinition = "VARCHAR", length = 250, nullable = false)
	private String city;

	@Column(columnDefinition = "VARCHAR", length = 250, nullable = false)
	private String address;

	/**
	 * @param name
	 * @param email
	 * @param phone
	 * @param industry
	 * @param country
	 * @param city
	 * @param address
	 */
	public Company(String name, String email, String phone, String industry, String country, String city,
			String address) {
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.industry = industry;
		this.country = country;
		this.city = city;
		this.address = address;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
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
