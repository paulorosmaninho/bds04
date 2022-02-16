package com.devsuperior.bds04.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds04.dto.EventDTO;
import com.devsuperior.bds04.entities.City;
import com.devsuperior.bds04.entities.Event;
import com.devsuperior.bds04.repositories.CityRepository;
import com.devsuperior.bds04.repositories.EventRepository;
import com.devsuperior.bds04.services.exceptions.DatabaseException;
import com.devsuperior.bds04.services.exceptions.ResourceNotFoundException;

@Service
public class EventService {

	@Autowired
	EventRepository eventRepository;

	@Autowired
	private CityRepository cityRepository;

	@Transactional(readOnly = true)
	public Page<EventDTO> findAllPaged(Pageable pageable) {

		Page<Event> pageEntity = eventRepository.findAll(pageable);

		Page<EventDTO> pageEventDTO = pageEntity.map(elementEntity -> new EventDTO(elementEntity));

		return pageEventDTO;

	}

	@Transactional(readOnly = true)
	public EventDTO findById(Long id) {

		Optional<Event> objOptional = eventRepository.findById(id);

		Event entity = objOptional.orElseThrow(() -> new ResourceNotFoundException("Event " + id + " não encontrado"));

		return new EventDTO(entity);
	}

	@Transactional
	public EventDTO insert(EventDTO eventDTO) {
		
			Event entity = new Event(); 
			
			copyDtoToEntity(eventDTO, entity);
			
			entity.setCity(new City(eventDTO.getCityId(), null));
			
			entity = eventRepository.save(entity);
			
			return new EventDTO(entity);
			
	}
	
	@Transactional
	public EventDTO update(Long id, EventDTO eventDTO) {

		try {

			Event entity = eventRepository.getOne(id);
			City city = cityRepository.getOne(eventDTO.getCityId());

			copyDtoToEntity(eventDTO, entity);

			entity.setCity(city);

			entity = eventRepository.save(entity);

			return new EventDTO(entity);

		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Evento " + id + " não encontrado");
		}
	}

	public void delete(Long id) {

		try {

			eventRepository.deleteById(id);

		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Evento " + id + " não encontrado");
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	public void copyDtoToEntity(EventDTO dto, Event entity) {

		entity.setName(dto.getName());
		entity.setDate(dto.getDate());
		entity.setUrl(dto.getUrl());

	}

}
