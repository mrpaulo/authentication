package com.paulorodrigues.authentication.person.controller;


import com.paulorodrigues.authentication.commons.exception.InvalidRequestException;
import com.paulorodrigues.authentication.commons.exception.NotFoundException;
import com.paulorodrigues.authentication.commons.exception.ValidationException;
import com.paulorodrigues.authentication.commons.model.PersonDTO;
import com.paulorodrigues.authentication.commons.model.PersonRequest;
import com.paulorodrigues.authentication.commons.model.PersonResponse;
import com.paulorodrigues.authentication.commons.resource.PersonResource;

import com.paulorodrigues.authentication.person.service.PersonService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@RestController

public class PersonController implements PersonResource {

    @Autowired
    private PersonService personService;

    public ResponseEntity<List<PersonDTO>> findAll() {
        try {
            List<PersonDTO> peopleSorted = personService.findAll()
                    .stream()
                    .sorted(Comparator.comparing(PersonDTO::getFirstName))
                    .collect(Collectors.toList());
            return ResponseEntity.ok().body(peopleSorted);
        } catch (Exception e) {
            log.error("Exception on getAll message={}", e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    public ResponseEntity<PersonResponse> findByQueryPageable(@RequestBody PersonRequest personRequest) {
        try {
            return ResponseEntity.ok().body(personService.findByQueryPageable(personRequest));
        } catch (Exception e) {
            log.error("Exception on findPageable message={}", e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    public ResponseEntity<PersonDTO> findById(@PathVariable(value = "id") Long personId) throws NotFoundException {
        try {
            return ResponseEntity.ok().body(personService.findByIdDTO(personId));
        } catch (Exception e) {
            log.error("Exception on getById personId={}, message={}", personId, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    public ResponseEntity<PersonResponse> findByName(@RequestBody PersonRequest personRequest) throws InvalidRequestException {
        try {
            return ResponseEntity.ok().body(personService.findByName(personRequest));
        } catch (Exception e) {
            log.error("Exception on getByName personName={}, message={}", personRequest.getQuery().getName(), e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    public ResponseEntity<PersonDTO> create(@RequestBody PersonDTO person) throws ValidationException, InvalidRequestException {
        try {
            return new ResponseEntity<>(personService.create(person), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Exception on create person={}, message={}", person, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    public ResponseEntity<PersonDTO> update(@PathVariable(value = "id") Long personId, @RequestBody PersonDTO personDTO) throws NotFoundException, ValidationException, InvalidRequestException {
        try {
            return ResponseEntity.ok().body(personService.update(personId, personDTO));
        } catch (Exception e) {
            log.error("Exception on getByName personId={}, message={}", personId, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    public Map<String, Long> delete(@PathVariable(value = "id") Long personId) throws NotFoundException {
        try {
            personService.delete(personId);
            Map<String, Long> response = new HashMap<>();
            response.put("id", personId);
            return response;
        } catch (Exception e) {
            log.error("Exception on getByName personId={}, message={}", personId, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }
}
