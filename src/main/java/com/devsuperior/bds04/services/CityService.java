package com.devsuperior.bds04.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds04.services.exceptions.DatabaseException;
import com.devsuperior.bds04.services.exceptions.ResourceNotFoundException;
import com.devsuperior.bds04.dto.CityDTO;
import com.devsuperior.bds04.entities.City;
import com.devsuperior.bds04.repositories.CityRepository;

@Service
public class CityService {

	@Autowired
	CityRepository cityRepository;

	@Transactional(readOnly = true)
	public List<CityDTO> findAll() {

		List<City> listEntityCities = cityRepository.findAll(Sort.by("name"));

		List<CityDTO> listDTOCities = new ArrayList<>();

		listEntityCities.forEach(entityCity -> listDTOCities.add(new CityDTO(entityCity)));

		return listDTOCities;

	}

	@Transactional(readOnly = true)
	public CityDTO findById(Long id) {

		Optional<City> objOptional = cityRepository.findById(id);
		
		City entity = objOptional.orElseThrow(() -> new ResourceNotFoundException("Cidade " + id + " não encontrada"));
		
		return new CityDTO(entity);
	}
	
	
	@Transactional
	public CityDTO insert(CityDTO cityDTO) {

		City entity = new City();

		entity.setName(cityDTO.getName());

		entity = cityRepository.save(entity);

		return new CityDTO(entity);

	}

	@Transactional
	public CityDTO update(Long id, CityDTO cityDTO) {

		try {
			
			City entity = cityRepository.getOne(id);

			entity.setName(cityDTO.getName());

			entity = cityRepository.save(entity);

			return new CityDTO(entity);
			
		} catch (EntityNotFoundException e) {

			throw new ResourceNotFoundException("Cidade " + id + " não encontrada");

		}

	}

	public void delete(Long id) {

		try {
			cityRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Cidade " + id + " não encontrada");
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}

	}

}
