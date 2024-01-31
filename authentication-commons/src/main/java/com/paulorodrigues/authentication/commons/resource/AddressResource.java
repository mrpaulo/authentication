package com.paulorodrigues.authentication.commons.resource;

import com.paulorodrigues.authentication.commons.exception.InvalidRequestException;
import com.paulorodrigues.authentication.commons.exception.NotFoundException;
import com.paulorodrigues.authentication.commons.model.AddressDTO;
import com.paulorodrigues.authentication.commons.model.CityDTO;
import com.paulorodrigues.authentication.commons.model.CountryDTO;
import com.paulorodrigues.authentication.commons.model.StateDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.paulorodrigues.authentication.commons.util.ConstantsUtil.*;

@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ok success"),
        @ApiResponse(responseCode = "201", description = "Address created"),
        @ApiResponse(responseCode = "400", description = "Validation Error Response"),
        @ApiResponse(responseCode = "401", description = "Full Authentication Required or Invalid access token"),
        @ApiResponse(responseCode = "403", description = "Insufficient scope"),
        @ApiResponse(responseCode = "404", description = "Not found"),
        @ApiResponse(responseCode = "500", description = "Something Unexpected Happened"),})
@RequestMapping(ADDRESSES_V1_BASE_API)
public interface AddressResource {

    @ApiOperation(value = "Get the address by id",
            notes = "It returns the address given an Id")
    @GetMapping(FIND_BY_ID_PATH)
    ResponseEntity<AddressDTO> getById(@PathVariable(value = "id") Long addressId) throws NotFoundException;

    @ApiOperation(value = "Get the address by person id",
            notes = "It returns the address given an personId")
    @GetMapping(FIND_BY_ID_PATH)
    ResponseEntity<AddressDTO> getByPersonId(@PathVariable(value = "id") Long personId) throws NotFoundException;

    @GetMapping(FIND_BY_NAME_PATH)
    ResponseEntity<List<AddressDTO>> getByName(@PathVariable(value = "name") String addressName);

    @PostMapping()
    ResponseEntity<AddressDTO> create(@RequestBody AddressDTO address) throws InvalidRequestException;

    @PutMapping(UPDATE_PATH)
    public ResponseEntity<AddressDTO> update(@PathVariable(value = "id") Long addressId, @RequestBody AddressDTO addressDTO) throws InvalidRequestException, NotFoundException;

    @DeleteMapping(DELETE_PATH)
    Map<String, Boolean> delete(@PathVariable(value = "id") Long addressId) throws NotFoundException;

    @GetMapping(GET_LOGRADOUROS)
    ResponseEntity<List<Map<String, String>>> getLogradouros();

    @GetMapping(GET_CITIES_PATH)
    ResponseEntity<List<CityDTO>> getAllCities(@PathVariable(value = "country") Long countryId, @PathVariable(value = "state") Long stateId);

    @GetMapping(GET_STATES_PATH)
    ResponseEntity<List<StateDTO>> getAllStates(@PathVariable(value = "country") Long countryId);

    @GetMapping(GET_COUNTRIES_PATH)
    ResponseEntity<List<CountryDTO>> getAllCountries();
}
