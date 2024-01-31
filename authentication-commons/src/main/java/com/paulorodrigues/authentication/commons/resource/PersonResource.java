package com.paulorodrigues.authentication.commons.resource;

import com.paulorodrigues.authentication.commons.exception.InvalidRequestException;
import com.paulorodrigues.authentication.commons.exception.ValidationException;
import com.paulorodrigues.authentication.commons.model.PersonDTO;
import com.paulorodrigues.authentication.commons.model.PersonRequest;
import com.paulorodrigues.authentication.commons.model.PersonResponse;
import com.paulorodrigues.authentication.commons.exception.NotFoundException;
import com.paulorodrigues.authentication.commons.util.ConstantsUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(ConstantsUtil.PEOPLE_V1_BASE_API)
public interface PersonResource {

    @GetMapping(ConstantsUtil.FIND_ALL_PATH)
    ResponseEntity<List<PersonDTO>> findAll();

    @PostMapping(ConstantsUtil.FIND_PAGEABLE_PATH)
    ResponseEntity<PersonResponse> findByQueryPageable(@RequestBody PersonRequest personRequest);

    @GetMapping(ConstantsUtil.FIND_BY_ID_PATH)
    ResponseEntity<PersonDTO> findById(@PathVariable(value = "id") Long personId) throws NotFoundException;

    @PostMapping(ConstantsUtil.FIND_BY_NAME_PAGEABLE_PATH)
    ResponseEntity<PersonResponse> findByName(@RequestBody PersonRequest personRequest) throws InvalidRequestException;

    @PostMapping()
    ResponseEntity<PersonDTO> create(@RequestBody PersonDTO person) throws ValidationException, InvalidRequestException;

    @PutMapping(ConstantsUtil.UPDATE_PATH)
    ResponseEntity<PersonDTO> update(@PathVariable(value = "id") Long personId, @RequestBody PersonDTO personDTO) throws NotFoundException, ValidationException, InvalidRequestException;

    @DeleteMapping(ConstantsUtil.DELETE_PATH)
    Map<String, Long> delete(@PathVariable(value = "id") Long personId) throws NotFoundException;
}
