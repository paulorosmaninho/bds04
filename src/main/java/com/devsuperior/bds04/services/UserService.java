package com.devsuperior.bds04.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds04.dto.UserDTO;
import com.devsuperior.bds04.dto.UserInsertDTO;
import com.devsuperior.bds04.dto.UserUpdateDTO;
import com.devsuperior.bds04.entities.User;
import com.devsuperior.bds04.repositories.UserRepository;
import com.devsuperior.bds04.services.exceptions.DatabaseException;
import com.devsuperior.bds04.services.exceptions.ResourceNotFoundException;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable) {

		Page<User> pageEntity = userRepository.findAll(pageable);

		Page<UserDTO> pageUserDTO = pageEntity.map(elementEntity -> new UserDTO(elementEntity));

		return pageUserDTO;

	}

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {

		Optional<User> objOptional = userRepository.findById(id);

		User entity = objOptional.orElseThrow(() -> new ResourceNotFoundException("User " + id + " não encontrado"));

		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO insert(UserInsertDTO userInsertDTO) {

		User entity = new User();

		copyDtoToEntity(userInsertDTO, entity);

		entity.setPassword(passwordEncoder.encode(userInsertDTO.getPassword()));

		entity = userRepository.save(entity);

		return new UserDTO(entity);

	}

	@Transactional
	public UserDTO update(Long id, UserUpdateDTO userUpdateDTO) {

		try {
			User entity = userRepository.getOne(id);

			copyDtoToEntity(userUpdateDTO, entity);

			entity = userRepository.save(entity);

			return new UserDTO(entity);

		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("User " + id + " não encontrado");
		}

	}

	public void delete(Long id) {

		try {

			userRepository.deleteById(id);

		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("User " + id + " não encontrado");
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	public void copyDtoToEntity(UserDTO dto, User entity) {

		entity.setEmail(dto.getEmail());

	}

}
