package com.paulorodrigues.authentication.commons.resource;

import com.paulorodrigues.authentication.commons.exception.InvalidRequestException;
import com.paulorodrigues.authentication.commons.exception.NotFoundException;
import com.paulorodrigues.authentication.commons.model.RoleDTO;
import com.paulorodrigues.authentication.commons.model.UpdatePassword;
import com.paulorodrigues.authentication.commons.model.UserDTO;
import com.paulorodrigues.authentication.commons.model.UserQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static com.paulorodrigues.authentication.commons.util.ConstantsUtil.*;

@RequestMapping(USERS_V1_BASE_API)
public interface UserResource {

    @PostMapping(FIND_PAGEABLE_PATH)
    List<UserDTO> findPageable(@RequestBody UserQuery filter, HttpServletRequest req, HttpServletResponse res);

    @GetMapping(GET_BY_ID_PATH)
    ResponseEntity<UserDTO> getById(@PathVariable(value = "id") Long userId) throws NotFoundException;

    @GetMapping(GET_BY_NAME_PATH)
    ResponseEntity<List<UserDTO>> getByName(@PathVariable(value = "name") String nameOfUser);

    @PostMapping()
    ResponseEntity<UserDTO> create(@RequestBody UserDTO user) throws InvalidRequestException;

    @PutMapping(UPDATE_PATH)
    ResponseEntity<UserDTO> update(@PathVariable(value = "id") Long userId, @RequestBody UserDTO userDTO) throws InvalidRequestException, NotFoundException;

    @DeleteMapping(DELETE_PATH)
    Map<String, Long> delete(@PathVariable(value = "id") Long userId) throws NotFoundException;

    @PostMapping(UPDATE_USER_PATH)
    ResponseEntity<String> changeUserPassword(@RequestBody UpdatePassword updatePassword) throws InvalidRequestException;

    @GetMapping(GET_ROLES_PATH)
    ResponseEntity<List<RoleDTO>> getAllRoles();

}
