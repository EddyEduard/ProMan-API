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

@Table(name = "account_roles")
@Entity
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class AccountRole {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "INT")
	private Long account_id;

	@Column(columnDefinition = "INT")
	private Long role_id;

	/**
	 * @param account_id
	 * @param role_id
	 */
	public AccountRole(Long account_id, Long role_id) {
		this.account_id = account_id;
		this.role_id = role_id;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the account_id
	 */
	public Long getAccount_id() {
		return account_id;
	}

	/**
	 * @return the role_id
	 */
	public Long getRole_id() {
		return role_id;
	}
}
