package com.team.proman.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.team.proman.model.db.Account;
import com.team.proman.repositories.AccountRepository;

@Service("accountService")
public class AccountService implements UserDetailsService {

	private final AccountRepository accountRepository;
	
	@Value("${aes.key}")
	private String aesKey;

	/**
	 * @param accountRepository
	 */
	public AccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
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
	 * Select accounts by company id.
	 * 
	 * @param id
	 * @return found accounts
	 */
	public List<Account> selectByCompanyId(Long id) {
		List<Account> foundAccounts = new ArrayList<Account>();
		Iterator<Account> accounts = accountRepository.findAll().iterator();

		while (accounts.hasNext()) {
			Account account = accounts.next();
			if (account.getCompany_id().equals(id)) {
				foundAccounts.add(account);
			}
		}

		return foundAccounts;
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
	
	/**
	 * Create a new account.
	 * 
	 * @param account
	 * @return created account
	 */
	public Account create(Account account) {
		AES256TextEncryptor aesEncryptor = new AES256TextEncryptor();
	    aesEncryptor.setPassword(aesKey);
	    account.setPassword(aesEncryptor.encrypt(account.getPassword()));
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
		foundAccount.setPhone(account.getPhone());

		AES256TextEncryptor aesEncryptor = new AES256TextEncryptor();
	    aesEncryptor.setPassword(aesKey);
	    foundAccount.setPassword(aesEncryptor.encrypt(account.getPassword()));

		return accountRepository.save(foundAccount);
	}
	
	/**
	 * Remove an account by id.
	 * 
	 * @param account
	 */
	public void delete(Account account) {
		accountRepository.delete(account);
	}
	
	/**
	 * Delete an account by company id.
	 * 
	 * @param companyId
	 */
	public void deleteByCompanyId(Long companyId) {
		Iterator<Account> accounts = accountRepository.findAll().iterator();

		while (accounts.hasNext()) {
			Account account = accounts.next();
			if (account.getCompany_id().equals(companyId)) {
				accountRepository.delete(account);
			}
		}
	}
}
