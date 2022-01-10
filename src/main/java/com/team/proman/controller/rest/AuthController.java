package com.team.proman.controller.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.proman.component.JwtToken;
import com.team.proman.model.AccountModel;
import com.team.proman.model.CompanyModel;
import com.team.proman.model.LoginModel;
import com.team.proman.model.RegisterModel;
import com.team.proman.model.TokenModel;
import com.team.proman.model.db.Account;
import com.team.proman.model.db.AccountRole;
import com.team.proman.model.db.Company;
import com.team.proman.model.db.Role;
import com.team.proman.services.AccountRoleService;
import com.team.proman.services.AccountService;
import com.team.proman.services.CompanyService;
import com.team.proman.services.RoleService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private AccountRoleService accountRoleService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private JwtToken jwtTokenUtil;

	/**
	 * Login user in account.
	 * 
	 * @param login
	 * @param bindingResult
	 * @return token model
	 */
	@Validated
	@PostMapping("/login")
	public ResponseEntity<Object> login(@Valid @RequestBody LoginModel login, BindingResult bindingResult) {

		if (bindingResult.hasErrors())
			return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);

		try {
			Account foundAccount = accountService.findByEmail(login.getEmail());

			if (foundAccount == null)
				return new ResponseEntity<>("There isn't an account with this email address.", HttpStatus.NOT_FOUND);

			if (!BCrypt.checkpw(login.getPassword(), foundAccount.getPassword()))
				return new ResponseEntity<>("There isn't an account with this password.", HttpStatus.NOT_FOUND);

			Set<SimpleGrantedAuthority> authorities = accountService.getAuthority(foundAccount);
			UserDetails userDetails = new User(foundAccount.getId().toString(), foundAccount.getPassword(),
					authorities);
			String token = jwtTokenUtil.generateToken(userDetails);
			String username = jwtTokenUtil.getUsernameFromToken(token);
			Date expiration = jwtTokenUtil.getExpirationDateFromToken(token);

			TokenModel newToken = new TokenModel();
			newToken.setToken(token);
			newToken.setUsername(username);
			newToken.setExpiration(expiration);

			return new ResponseEntity<>(newToken, HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Register user in account.
	 * 
	 * @param account
	 * @param bindingResult
	 * @return created account
	 */
	@Validated
	@PostMapping("/register")
	public ResponseEntity<Object> register(@Valid @RequestBody RegisterModel register, BindingResult bindingResult) {

		if (bindingResult.hasErrors())
			return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);

		try {
			AccountModel account = register.getAccount();
			CompanyModel company = register.getCompany();

			Company newCompany = companyService.create(company.getCompany());
			Account newAccount = accountService.create(account.getAccount(newCompany.getId()));
			Set<Role> setNewRole = new HashSet<Role>();

			for (String roleName : account.getRoles()) {
				Role foundRoleByName = roleService.findByName(roleName);
				if (foundRoleByName != null) {
					AccountRole accountRole = new AccountRole(newAccount.getId(), foundRoleByName.getId());
					AccountRole newAccountRole = accountRoleService.create(accountRole);
					Role foundRoleById = roleService.findById(newAccountRole.getRole_id());
					if (foundRoleById != null)
						setNewRole.add(foundRoleById);
				}
			}

			newAccount.setRoles(setNewRole);

			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("account", newAccount);
			result.put("company", newCompany);

			return new ResponseEntity<>(result, HttpStatus.CREATED);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Get profile account.
	 * 
	 * @param authentication
	 * @return account profile
	 */
	@GetMapping("/profile_account")
	public ResponseEntity<Object> profile_account(Authentication authentication) {
		Long id = Long.parseLong(authentication.getName());

		try {
			Account account = accountService.findById(id);
			return new ResponseEntity<>(account, HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Update profile account.
	 * 
	 * @param account
	 * @param bindingResult
	 * @param authentication
	 * @return updated account
	 */
	@Validated
	@PutMapping("/profile_account/{company_id}")
	public ResponseEntity<Object> update_profile_account(@Valid @RequestBody AccountModel account,
			@PathVariable(required = true, name = "company_id") Long company_id, BindingResult bindingResult,
			Authentication authentication) {
		if (bindingResult.hasErrors())
			return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);

		Long id = Long.parseLong(authentication.getName());

		try {
			Account foundAccount = accountService.findById(id);

			if (foundAccount == null)
				return new ResponseEntity<>("There isn't an account with this id.", HttpStatus.NOT_FOUND);

			for (Role role : foundAccount.getRoles()) {
				Integer existRole = account.getRoles().indexOf(role.getName());
				if (existRole.equals(-1)) {
					accountRoleService.deleteByAccountIdAndRoleId(foundAccount.getId(), role.getId());
				}
			}

			Set<Role> setNewRole = new HashSet<Role>();

			for (String roleName : account.getRoles()) {
				Role foundRole = null;

				for (Role role : foundAccount.getRoles()) {
					if (role.getName().equals(roleName)) {
						foundRole = role;
						break;
					}
				}

				if (foundRole == null) {
					Role foundRoleByName = roleService.findByName(roleName);
					if (foundRoleByName != null) {
						AccountRole accountRole = new AccountRole(foundAccount.getId(), foundRoleByName.getId());
						AccountRole newAccountRole = accountRoleService.create(accountRole);
						Role foundRoleById = roleService.findById(newAccountRole.getRole_id());
						if (foundRoleById != null)
							setNewRole.add(foundRoleById);
					}
				} else 
					setNewRole.add(foundRole);
			}

			Account updateAccount = accountService.update(id, account.getAccount(company_id));
			updateAccount.setRoles(setNewRole);

			return new ResponseEntity<>(updateAccount, HttpStatus.CREATED);
		} catch (

		Exception ex) {
			return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
		}
	}
}
