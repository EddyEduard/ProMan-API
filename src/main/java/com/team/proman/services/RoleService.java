package com.team.proman.services;

import java.util.Iterator;

import org.springframework.stereotype.Service;

import com.team.proman.model.db.Role;
import com.team.proman.repositories.RoleRepository;

@Service("roleService")
public class RoleService {

	private final RoleRepository roleRepository;

	/**
	 * @param roleRepository
	 */
	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}
	
	/**
	 * Find a role by id.
	 * 
	 * @param id
	 * @return found role or null
	 */
	public Role findById(Long id) {
		Iterator<Role> roles = roleRepository.findAll().iterator();

		while (roles.hasNext()) {
			Role role = roles.next();
			if (role.getId().equals(id)) {
				return role;
			}
		}

		return null;
	}
	
	/**
	 * Find a role by name.
	 * 
	 * @param name
	 * @return found role or null
	 */
	public Role findByName(String name) {
		Iterator<Role> roles = roleRepository.findAll().iterator();

		while (roles.hasNext()) {
			Role role = roles.next();
			if (role.getName().equals(name)) {
				return role;
			}
		}

		return null;
	}
}
