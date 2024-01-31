/*
 * Copyright (C) 2021 paulo.rodrigues
 * Profile: <https://github.com/mrpaulo>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.paulorodrigues.authentication.address.controller;



import com.paulorodrigues.authentication.address.service.AddressService;
import com.paulorodrigues.authentication.commons.exception.InvalidRequestException;
import com.paulorodrigues.authentication.commons.exception.NotFoundException;
import com.paulorodrigues.authentication.commons.model.AddressDTO;
import com.paulorodrigues.authentication.commons.model.CityDTO;
import com.paulorodrigues.authentication.commons.model.CountryDTO;
import com.paulorodrigues.authentication.commons.model.StateDTO;
import com.paulorodrigues.authentication.commons.resource.AddressResource;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author paulo.rodrigues
 */
@Log4j2
@RestController
@CrossOrigin(origins = {"*"})
public class AddressController implements AddressResource {

    @Autowired
    private AddressService addressService;


    public ResponseEntity<AddressDTO> getById(@PathVariable(value = "id") Long addressId) throws NotFoundException {
        try {
            return ResponseEntity.ok().body(addressService.findDTOById(addressId));
        } catch (Exception e) {
            log.error("Exception on getById addressId={}, message={}", addressId, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    public ResponseEntity<AddressDTO> getByPersonId(@PathVariable(value = "id") Long personId) throws NotFoundException {
        try {
            return ResponseEntity.ok().body(addressService.findByPersonId(personId));
        } catch (Exception e) {
            log.error("Exception on getByPersonId addressId={}, message={}", personId, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    public ResponseEntity<List<AddressDTO>> getByName(@PathVariable(value = "name") String addressName) {
        try {
            return ResponseEntity.ok().body(addressService.findByName(addressName));
        } catch (Exception e) {
            log.error("Exception on getByName addressName={}, message={}", addressName, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    public ResponseEntity<AddressDTO> create(@RequestBody AddressDTO address) throws InvalidRequestException {
        try {
            return new ResponseEntity<>(addressService.create(address), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Exception on create address={}, message={}", address, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    public ResponseEntity<AddressDTO> update(@PathVariable(value = "id") Long addressId, @RequestBody AddressDTO addressDTO) throws InvalidRequestException, NotFoundException {
        try {
            return ResponseEntity.ok(addressService.edit(addressId, addressDTO));
        } catch (Exception e) {
            log.error("Exception on update addressId={}, message={}", addressId, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    public Map<String, Boolean> delete(@PathVariable(value = "id") Long addressId) throws NotFoundException {
        try {
            addressService.delete(addressId);
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return response;
        } catch (Exception e) {
            log.error("Exception on delete addressId={}, message={}", addressId, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }
    
    public ResponseEntity<List<Map<String, String>>> getLogradouros() {
        try {
            return ResponseEntity.ok().body(addressService.getLogradouros());
        } catch (Exception e) {
            log.error("Exception on getETypePublicPlace message={}", e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }
    
    public ResponseEntity<List<CityDTO>> getAllCities(@PathVariable(value = "country") Long countryId, @PathVariable(value = "state") Long stateId) {
        try {
            return ResponseEntity.ok().body(addressService.getAllCities(countryId, stateId));
        } catch (Exception e) {
            log.error("Exception on getAllCities countryId={}, stateId={}, message={}", countryId, stateId, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }
    
    public ResponseEntity<List<StateDTO>> getAllStates(@PathVariable(value = "country") Long countryId) {
        try {
            return ResponseEntity.ok().body(addressService.getAllStates(countryId));
        } catch (Exception e) {
            log.error("Exception on getAllStates countryId={}, message={}", countryId, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }
    
    public ResponseEntity<List<CountryDTO>> getAllCountries() {
        try {
            return ResponseEntity.ok().body(addressService.getAllCountries());
        } catch (Exception e) {
            log.error("Exception on getAllCountries message={}", e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }
}
