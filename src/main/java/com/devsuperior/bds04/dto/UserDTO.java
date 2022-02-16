package com.devsuperior.bds04.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;

import com.devsuperior.bds04.entities.User;

public class UserDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	@Email(message = "Email inválido")
	private String email;
	
	private Set<RoleDTO> rolesDTO = new HashSet<>();

	public UserDTO() {
	}

	public UserDTO(Long id, String email) {
		super();
		this.id = id;
		this.email = email;
	}

	public UserDTO(User entity) {
		this.id = entity.getId();
		this.email = entity.getEmail();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<RoleDTO> getRolesDTO() {
		return rolesDTO;
	}

}
