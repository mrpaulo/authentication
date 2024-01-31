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
package com.paulorodrigues.authentication.address.service;


import com.paulorodrigues.authentication.address.entity.Address;
import com.paulorodrigues.authentication.address.entity.City;
import com.paulorodrigues.authentication.address.entity.Country;
import com.paulorodrigues.authentication.address.entity.StateCountry;
import com.paulorodrigues.authentication.address.repository.AddressRepository;
import com.paulorodrigues.authentication.address.repository.CityRepository;
import com.paulorodrigues.authentication.address.repository.CountryRepository;
import com.paulorodrigues.authentication.address.repository.StateCountryRepository;
import com.paulorodrigues.authentication.commons.enums.TipoLogradouro;
import com.paulorodrigues.authentication.commons.exception.InvalidRequestException;
import com.paulorodrigues.authentication.commons.exception.NotFoundException;
import com.paulorodrigues.authentication.commons.model.AddressDTO;
import com.paulorodrigues.authentication.commons.model.CityDTO;
import com.paulorodrigues.authentication.commons.model.CountryDTO;
import com.paulorodrigues.authentication.commons.model.StateDTO;
import com.paulorodrigues.authentication.commons.util.MessageUtil;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author paulo.rodrigues
 */
@Service
@Transactional
@Log4j2
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;
    
    @Autowired
    private CityRepository cityRepository;
    
    @Autowired
    private StateCountryRepository stateCountryRepository;
    
    @Autowired
    private CountryRepository countryRepository;

    public Address findById(Long addressId) throws NotFoundException {
        log.info("Finding address by addressId={}", addressId);
        return addressRepository.findById(addressId)
               .orElseThrow(
                        () -> new NotFoundException(MessageUtil.getMessage("ADDRESS_NOT_FOUND") + " ID: " + addressId)
                );
    }

    public AddressDTO findDTOById(Long addressId) throws NotFoundException {
        return findById(addressId).toDTO();
    }

    public AddressDTO findByPersonId(Long personId) throws NotFoundException {
        log.info("Finding address by personId={}", personId);
        return addressRepository.findById(personId)
               .orElseThrow(
                        () -> new NotFoundException(MessageUtil.getMessage("ADDRESS_NOT_FOUND") + " ID: " + personId)
                ).toDTO();
    }

    public AddressDTO create(AddressDTO address) throws InvalidRequestException {
        log.info("Creating address name={}", address.getName());
        return save(transformAddressFromDTO(address)).toDTO();
    }

    public Address save(Address address) throws InvalidRequestException {
        address.addressValidation();
        address.persistAt();
        log.info("Saving address={}", address);
        return addressRepository.saveAndFlush(address);
    }

    public AddressDTO edit(Long addressId, AddressDTO addressDetail) throws InvalidRequestException, NotFoundException {
        Address addressToEdit = findById(addressId);
        String createBy = addressToEdit.getCreateBy();
        var createAt = addressToEdit.getCreateAt();
        ModelMapper modelMapper = new ModelMapper();
        addressToEdit = modelMapper.map(addressDetail, Address.class);
        addressToEdit.setCreateBy(createBy);
        addressToEdit.setCreateAt(createAt);
        addressToEdit.setId(addressId);
        log.info("Updating address id={}, name={}", addressId, addressToEdit.getName());
        return save(addressToEdit).toDTO();
    }

    public void delete(Long addressId) throws NotFoundException {
        Address addressToDelete = findById(addressId);
        log.info("Deleting address id={}, name={}", addressId, addressToDelete.getName());
        addressRepository.delete(addressToDelete);
    }

    public Address getAddressFromDTO (AddressDTO dto) {
        try {
            return Objects.nonNull(dto) ? findById(dto.getId()) : null;
        } catch (Exception e) {
            log.error("Exception on getAddressFromDTO addressDTO={}, message={}", dto, e.getMessage());
            return null;
        }
    }

    public Address transformAddressFromDTO (AddressDTO dto) {
        return Address.builder()
                .id(dto.getId())
                .logradouro(dto.getLogradouro())
                .city(getCityFromDTO(dto.getCity()))
                .name(dto.getName())
                .number(dto.getNumber())
                .cep(dto.getCep())
                .zipCode(dto.getZipCode())
                .neighborhood(dto.getNeighborhood())
                .coordination(dto.getCoordination())
                .referentialPoint(dto.getReferentialPoint())
                .build();
    }

    public City getCityFromDTO (CityDTO dto) {
        try {
            return Objects.nonNull(dto) ?
                    cityRepository.findById(dto.getId()).orElse(null) :
                    null;
        } catch (Exception e) {
            log.error("Exception on getCityFromDTO CityDTO={}, message={}", dto, e.getMessage());
            return null;
        }
    }

    public Country getCountryFromDTO (CountryDTO dto) {
        try {
            return Objects.nonNull(dto) ?
                    countryRepository.findById(dto.getId()).orElse(null) :
                    null;
        } catch (Exception e) {
            log.error("Exception on getCountryFromDTO CountryDTO={}, message={}", dto, e.getMessage());
            return null;
        }
    }

    public List<Map<String, String>> getLogradouros() {
        return Stream.of(TipoLogradouro.values()).map(temp -> {
            Map<String, String> obj = new HashMap<>();
            obj.put("value", temp.getName());
            obj.put("label", temp.getDescription());
            return obj;
        }).collect(Collectors.toList());
    }

     public List<CountryDTO> getAllCountries(){
        return countryRepository.findAll().stream().map(Country::toDTO).collect(Collectors.toList());
    }
     
    public List<StateDTO> getAllStates(Long countryId){
        Optional<Country> country = countryRepository.findById(countryId);
        return country.map(value ->
                        stateCountryRepository.findByCountry(value).stream().map(StateCountry::toDTO).collect(Collectors.toList()))
                .orElse(null);
    }
    
    public List<CityDTO> getAllCities(Long countryId, Long stateId){
        Country country = countryRepository.findById(countryId).orElse(null);
        StateCountry state = stateCountryRepository.findById(stateId).orElse(null);
        if(country == null || state == null){
            return null;
        }
        return cityRepository.findByCountryAndState(country, state).stream()
                .map(City::toDTO).collect(Collectors.toList());
    }

    public List<AddressDTO> findByName(String name) {
        return addressRepository.findByName(name).stream().map(Address::toDTO).collect(Collectors.toList());
    }
}
