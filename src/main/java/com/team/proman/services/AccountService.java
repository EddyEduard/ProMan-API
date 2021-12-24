package com.team.proman.services;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.team.proman.model.db.Account;
import com.team.proman.repositories.AccountRepository;

@Service("accountService")
public class AccountService implements UserDetailsService {

	private final AccountRepository accountRepository;

	/**
	 * @param accountRepository
	 */
	public AccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	/**
	 * Create a new account.
	 * 
	 * @param account
	 * @return created account
	 */
	public Account create(Account account) {
		account.setPassword(BCrypt.hashpw(account.getPassword(), BCrypt.gensalt()));
		return accountRepository.save(account);
	}

	/**
	 * Update an account by id.
	 * 
	 * @param id
	 * @param account
	 * @return updated account
	 */
	public Account update(Long id, Account account) {
		Account foundAccount = findById(id);
		foundAccount.setUsername(account.getUsername());
		foundAccount.setEmail(account.getEmail());
		foundAccount.setCountry(account.getCountry());
		foundAccount.setIndustry(account.getIndustry());
		foundAccount.setCompany(account.getCompany());
		foundAccount.setPhone(account.getPhone());
		foundAccount.setTeam_size(account.getTeam_size());

		if (!BCrypt.checkpw(account.getPassword(), foundAccount.getPassword()))
			foundAccount.setPassword(BCrypt.hashpw(account.getPassword(), BCrypt.gensalt()));

		return accountRepository.save(foundAccount);
	}

	/**
	 * Find an account by id.
	 * 
	 * @param id
	 * @return found account or null
	 */
	public Account findById(Long id) {
		Iterator<Account> accounts = accountRepository.findAll().iterator();

		while (accounts.hasNext()) {
			Account account = accounts.next();
			if (account.getId().equals(id)) {
				return account;
			}
		}

		return null;
	}

	/**
	 * Find an account by email.
	 * 
	 * @param email
	 * @return found account or null
	 */
	public Account findByEmail(String email) {
		Iterator<Account> accounts = accountRepository.findAll().iterator();

		while (accounts.hasNext()) {
			Account account = accounts.next();
			if (account.getEmail().equals(email)) {
				return account;
			}
		}

		return null;
	}

	/**
	 * Get authority roles.
	 * 
	 * @param account
	 * @return authorities account
	 */
	public Set<SimpleGrantedAuthority> getAuthority(Account account) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		account.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
		});
		return authorities;
	}

	/**
	 * Load an account by username (the username is account id).
	 * 
	 * @param username
	 * @return account details
	 */
	@Override
	public UserDetails loadUserByUsername(String username) {
		Account foundAccount = findById(Long.parseLong(username));

		Set<SimpleGrantedAuthority> authorities = getAuthority(foundAccount);

		if (foundAccount != null && foundAccount.getId().equals(Long.parseLong(username))) {
			return new User(foundAccount.getId().toString(), foundAccount.getPassword(), authorities);
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}
}
