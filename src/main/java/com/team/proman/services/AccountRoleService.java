package com.team.proman.services;

import java.util.Iterator;

import org.springframework.stereotype.Service;

import com.team.proman.model.db.AccountRole;
import com.team.proman.repositories.AccountRoleRepository;

@Service("accountRoleService")
public class AccountRoleService {

	private final AccountRoleRepository accountRoleRepository;

	/**
	 * @param roleRepository
	 */
	public AccountRoleService(AccountRoleRepository accountRoleRepository) {
		this.accountRoleRepository = accountRoleRepository;
	}

	/**
	 * Create a new account role.
	 * 
	 * @param accountRole
	 * @return created account role
	 */
	public AccountRole create(AccountRole accountRole) {
		return this.accountRoleRepository.save(accountRole);
	}

	/**
	 * Delete account role.
	 * 
	 * @param accountRole
	 */
	public void delete(AccountRole accountRole) {
		this.accountRoleRepository.delete(accountRole);
	}

	/**
	 * Delete an account role by account id and role id.
	 * 
	 * @param accountId
	 * @param roleId
	 */
	public void deleteByAccountIdAndRoleId(Long accountId, Long roleId) {
		Iterator<AccountRole> accountRoles = accountRoleRepository.findAll().iterator();

		while (accountRoles.hasNext()) {
			AccountRole accountRole = accountRoles.next();
			if (accountRole.getAccount_id().equals(accountId) && accountRole.getRole_id().equals(roleId)) {
				accountRoleRepository.delete(accountRole);
			}
		}
	}
	
	/**
	 * Find an account by account id.
	 * 
	 * @param id
	 * @return found account role or null
	 */
	public AccountRole findByAccountId(Long id) {
		Iterator<AccountRole> accountRoles = accountRoleRepository.findAll().iterator();

		while (accountRoles.hasNext()) {
			AccountRole accountRole = accountRoles.next();
			if (accountRole.getAccount_id().equals(id)) {
				return accountRole;
			}
		}

		return null;
	}
}
