package com.devsuperior.bds04.dto;

import javax.validation.constraints.NotBlank;

import com.devsuperior.bds04.services.validation.UserInsertValid;

@UserInsertValid
public class UserInsertDTO extends UserDTO{

	private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "A senha do usuário é um campo obrigatório")
	private String password;

	public UserInsertDTO() {
		super();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
