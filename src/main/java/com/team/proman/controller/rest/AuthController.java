package com.team.proman.controller.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
import com.team.proman.model.db.Project;
import com.team.proman.model.db.Role;
import com.team.proman.model.db.Sprint;
import com.team.proman.model.db.Task;
import com.team.proman.services.AccountRoleService;
import com.team.proman.services.AccountService;
import com.team.proman.services.CompanyService;
import com.team.proman.services.ProjectService;
import com.team.proman.services.RoleService;
import com.team.proman.services.SprintService;
import com.team.proman.services.TaskService;

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
	private ProjectService projectService;

	@Autowired
	private SprintService sprintService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private JwtToken jwtTokenUtil;

	@Value("${aes.key}")
	private String aesKey;

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
				return new ResponseEntity<>(new String[] { "There isn't an account with this email." },
						HttpStatus.NOT_FOUND);

			AES256TextEncryptor aesEncryptor = new AES256TextEncryptor();
			aesEncryptor.setPassword(aesKey);
			String decryptedPassword = aesEncryptor.decrypt(foundAccount.getPassword());

			if (!login.getPassword().equals(decryptedPassword))
				return new ResponseEntity<>(new String[] { "There isn't an account with this password." },
						HttpStatus.NOT_FOUND);

			Set<SimpleGrantedAuthority> authorities = accountService.getAuthority(foundAccount);
			UserDetails userDetails = new User(foundAccount.getId().toString(), foundAccount.getPassword(),
					authorities);
			String token = jwtTokenUtil.generateToken(userDetails);
			String username = jwtTokenUtil.getUsernameFromToken(token);
			Date expiration = jwtTokenUtil.getExpirationDateFromToken(token);

			List<String> roles = new ArrayList<String>();

			for (Role role : foundAccount.getRoles())
				roles.add(role.getName());

			TokenModel newToken = new TokenModel();
			newToken.setToken(token);
			newToken.setUsername(username);
			newToken.setRoles(roles);
			newToken.setExpiration(expiration);

			return new ResponseEntity<>(newToken, HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Create a new company profile with account.
	 * 
	 * @param register
	 * @param bindingResult
	 * @return created company profile
	 */
	@Validated
	@PostMapping("/register")
	public ResponseEntity<Object> register(@Valid @RequestBody RegisterModel register, BindingResult bindingResult) {

		if (bindingResult.hasErrors())
			return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);

		try {
			String username = register.getUsername();
			String password = register.getPassword();
			List<String> roles = register.getRoles();
			CompanyModel company = register.getCompany();

			Company newCompany = companyService.create(company.getCompany());
			Account account = new Account(newCompany.getId(), username, newCompany.getEmail(), password,
					newCompany.getPhone());
			Account newAccount = accountService.create(account);
			Set<Role> setNewRole = new HashSet<Role>();

			for (String roleName : roles) {
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
	 * Create a new account for a company.
	 * 
	 * @param account
	 * @param company_id
	 * @param bindingResult
	 * @param authentication
	 * @return created account
	 */
	@Validated
	@PatchMapping("/register")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Object> register(@Valid @RequestBody AccountModel account, BindingResult bindingResult,
			Authentication authentication) {

		if (bindingResult.hasErrors())
			return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);

		Long id = Long.parseLong(authentication.getName());

		try {
			Account foundAccount = accountService.findById(id);

			if (foundAccount == null)
				return new ResponseEntity<>(new String[] { "There isn't an account with this id." },
						HttpStatus.NOT_FOUND);

			Company foundCompany = companyService.findById(foundAccount.getCompany_id());

			if (foundCompany == null)
				return new ResponseEntity<>(new String[] { "There isn't a company with this id." },
						HttpStatus.NOT_FOUND);

			Account newAccount = accountService.create(account.getAccount(foundAccount.getCompany_id()));
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

			return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Get account profile.
	 * 
	 * @param authentication
	 * @return account profile
	 */
	@GetMapping("/account_profile")
	public ResponseEntity<Object> account_profile(
			@RequestParam(required = false, name = "decrypt_aes_key") String decrypt_aes_key,
			Authentication authentication) {
		Long id = Long.parseLong(authentication.getName());

		try {
			Account foundAccount = accountService.findById(id);

			if (foundAccount == null)
				return new ResponseEntity<>(new String[] { "There isn't an account with this id." },
						HttpStatus.NOT_FOUND);

			if (aesKey.equals(decrypt_aes_key)) {
				AES256TextEncryptor aesEncryptor = new AES256TextEncryptor();
				aesEncryptor.setPassword(decrypt_aes_key);
				foundAccount.setPassword(aesEncryptor.decrypt(foundAccount.getPassword()));
			}

			return new ResponseEntity<>(foundAccount, HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Get company profile.
	 * 
	 * @param authentication
	 * @return company profile
	 */
	@GetMapping("/company_profile")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Object> company_profile(Authentication authentication) {
		Long id = Long.parseLong(authentication.getName());

		try {
			Account foundAccount = accountService.findById(id);

			if (foundAccount == null)
				return new ResponseEntity<>(new String[] { "There isn't an account with this id." },
						HttpStatus.NOT_FOUND);

			Company foundCompany = companyService.findById(foundAccount.getCompany_id());

			if (foundCompany == null)
				return new ResponseEntity<>(new String[] { "There isn't a company with this id." },
						HttpStatus.NOT_FOUND);

			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("account", foundAccount);
			result.put("company", foundCompany);

			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Update account profile.
	 * 
	 * @param account
	 * @param bindingResult
	 * @param authentication
	 * @return updated account
	 */
	@Validated
	@PutMapping("/account_profile")
	public ResponseEntity<Object> account_profile(@Valid @RequestBody AccountModel account, BindingResult bindingResult,
			Authentication authentication) {
		if (bindingResult.hasErrors())
			return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);

		Long id = Long.parseLong(authentication.getName());

		try {
			Account foundAccount = accountService.findById(id);

			if (foundAccount == null)
				return new ResponseEntity<>(new String[] { "There isn't an account with this id." },
						HttpStatus.NOT_FOUND);

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

			Account updateAccount = accountService.update(id, account.getAccount(foundAccount.getCompany_id()));
			updateAccount.setRoles(setNewRole);

			return new ResponseEntity<>(updateAccount, HttpStatus.CREATED);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Update company profile.
	 * 
	 * @param company
	 * @param bindingResult
	 * @param authentication
	 * @return company profile
	 */
	@Validated
	@PutMapping("/company_profile")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Object> company_profile(@Valid @RequestBody CompanyModel company, BindingResult bindingResult,
			Authentication authentication) {
		if (bindingResult.hasErrors())
			return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);

		Long id = Long.parseLong(authentication.getName());

		try {
			Account foundAccount = accountService.findById(id);

			if (foundAccount == null)
				return new ResponseEntity<>(new String[] { "There isn't an account with this id." },
						HttpStatus.NOT_FOUND);

			Company updatedCompany = companyService.update(foundAccount.getCompany_id(), company.getCompany());

			return new ResponseEntity<>(updatedCompany, HttpStatus.CREATED);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Delete an account profile.
	 * 
	 * @param company
	 * @param authentication
	 * @return company profile
	 */
	@Validated
	@DeleteMapping("account_profile/{account_id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Object> account_profile(@PathVariable(required = true, name = "account_id") Long account_id,
			Authentication authentication) {

		Long id = Long.parseLong(authentication.getName());

		try {
			Account foundAccount = accountService.findById(account_id);

			if (foundAccount == null)
				return new ResponseEntity<>(new String[] { "There isn't an account with this id." },
						HttpStatus.NOT_FOUND);

			if (id.equals(account_id)) {
				Company foundCompany = companyService.findById(foundAccount.getCompany_id());

				if (foundCompany == null)
					return new ResponseEntity<>(new String[] { "There isn't a company with this id." },
							HttpStatus.NOT_FOUND);

				List<Account> accounts = accountService.selectByCompanyId(foundCompany.getId());

				for (Account account : accounts) {
					for (Role role : account.getRoles())
						accountRoleService.deleteByAccountIdAndRoleId(account.getId(), role.getId());

					accountService.deleteByCompanyId(account.getCompany_id());
				}

				List<Project> foundProjects = projectService.selectByCompanyId(foundCompany.getId());

				for (Project project : foundProjects) {
					List<Sprint> foundSprints = sprintService.selectByProjectId(project.getId());

					for (Sprint sprint : foundSprints) {
						List<Task> foundTasks = taskService.selectByProjectIdAndSprintId(project.getId(),
								sprint.getId());

						for (Task task : foundTasks)
							taskService.deleteById(task.getId());

						sprintService.deleteById(sprint.getId());
					}

					projectService.deleteById(project.getId());
				}

				companyService.delete(foundCompany);

				return new ResponseEntity<>(
						new String[] { "Your account has been deleted.Your company profile has been deleted." },
						HttpStatus.OK);
			} else {
				for (Role role : foundAccount.getRoles())
					accountRoleService.deleteByAccountIdAndRoleId(foundAccount.getId(), role.getId());

				accountService.delete(foundAccount);

				return new ResponseEntity<>(new String[] { "Your account has been deleted." }, HttpStatus.OK);
			}
		} catch (Exception ex) {
			return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
		}
	}
}
