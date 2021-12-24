package com.team.proman.model.db;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Table(name = "account")
@Entity
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "VARCHAR", length = 50, nullable = false)
	private String username;

	@Column(columnDefinition = "VARCHAR", unique = true, nullable = false)
	private String email;

	@Column(columnDefinition = "VARCHAR", length = 250, nullable = false)
	private String password;

	@Column(columnDefinition = "VARCHAR", length = 250, nullable = false)
	private String country;

	@Column(columnDefinition = "VARCHAR", length = 250, nullable = false)
	private String industry;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String company;

	@Column(columnDefinition = "VARCHAR", length = 12)
	private String phone;

	@Column(columnDefinition = "INT")
	private Integer team_size = 0;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "account_roles", joinColumns = { @JoinColumn(name = "account_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private Set<Role> roles;

	/**
	 * @param username
	 * @param email
	 * @param password
	 * @param country
	 * @param industry
	 * @param company
	 * @param phone
	 * @param teamSize
	 */
	public Account(String username, String email, String password, String country, String industry, String company,
			String phone, Integer team_size) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.country = country;
		this.industry = industry;
		this.company = company;
		this.phone = phone;
		this.team_size = team_size;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
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
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
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
	 * @return the team_size
	 */
	public Integer getTeam_size() {
		return team_size;
	}

	/**
	 * @param team_size the team_size to set
	 */
	public void setTeam_size(Integer team_size) {
		this.team_size = team_size;
	}

	/**
	 * @return the roles
	 */
	public Set<Role> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
}