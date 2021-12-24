package com.team.proman.controller.rest;

import java.util.Date;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.proman.component.JwtToken;
import com.team.proman.model.AccountModel;
import com.team.proman.model.LoginModel;
import com.team.proman.model.TokenModel;
import com.team.proman.model.db.Account;
import com.team.proman.services.AccountService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private JwtToken jwtTokenUtil;

	/**
	 * Login user in account.
	 * 
	 * @param login 
	 * @param bindingResult
	 * @return token model 
	 * */
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
	 * */
	@Validated
	@PostMapping("/register")
	public ResponseEntity<Object> register(@Valid @RequestBody AccountModel account, BindingResult bindingResult) {

		if (bindingResult.hasErrors())
			return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);

		try {
			Account newAccount = accountService.create(account.getAccount());

			return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Get profile account.
	 * 
	 * @param authentication
	 * @return account profile
	 * */
	@GetMapping("/profile")
	public ResponseEntity<Object> profile(Authentication authentication) {
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
	 * */
	@Validated
	@PutMapping("/profile")
	public ResponseEntity<Object> update_profile(@Valid @RequestBody AccountModel account, BindingResult bindingResult,
			Authentication authentication) {
		if (bindingResult.hasErrors())
			return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);

		Long id = Long.parseLong(authentication.getName());

		try {
			Account foundAccount = accountService.findById(id);

			if (foundAccount == null)
				return new ResponseEntity<>("There isn't an account with this id.", HttpStatus.NOT_FOUND);

			Account updateAccount = accountService.update(id, account.getAccount());

			return new ResponseEntity<>(updateAccount, HttpStatus.CREATED);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
		}
	}
}
