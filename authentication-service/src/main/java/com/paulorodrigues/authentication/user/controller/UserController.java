package com.paulorodrigues.authentication.user.controller;

import com.paulorodrigues.authentication.commons.exception.InvalidRequestException;
import com.paulorodrigues.authentication.commons.exception.NotFoundException;
import com.paulorodrigues.authentication.commons.model.RoleDTO;
import com.paulorodrigues.authentication.commons.model.UpdatePassword;
import com.paulorodrigues.authentication.commons.model.UserDTO;

import com.paulorodrigues.authentication.commons.model.UserQuery;
import com.paulorodrigues.authentication.commons.util.FormatUtil;
import com.paulorodrigues.authentication.commons.util.MessageUtil;
import com.paulorodrigues.authentication.user.entity.User;
import com.paulorodrigues.authentication.user.service.UserService;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author paulo.rodrigues
 */
@Log4j2
@RestController
@CrossOrigin(origins = {"*"})

public class UserController {

    @Autowired
    private UserService userService;

    public List<UserDTO> findPageable(@RequestBody UserQuery filter, HttpServletRequest req, HttpServletResponse res) {
        try {
            Pageable pageable = FormatUtil.getPageRequest(filter);
            Page<User> result = userService.findPageable(filter, pageable);
            res.addHeader("totalCount", String.valueOf(result.getTotalElements()));
            return userService.usersToDTOs(result.getContent()).stream().sorted(Comparator.comparing(UserDTO::getUsername)).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Exception on findPageable message={}", e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    public ResponseEntity<UserDTO> getById(@PathVariable(value = "id") Long userId) throws NotFoundException {
        try {
            User user = userService.findById(userId);
            return ResponseEntity.ok().body(user.toDTO().hidePassword());
        } catch (Exception e) {
            log.error("Exception on getById bookId={}, message={}", userId, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    public ResponseEntity<List<UserDTO>> getByName(@PathVariable(value = "name") String nameOfUser) {
        try {
            return ResponseEntity.ok().body(userService.findByName(nameOfUser));
        } catch (Exception e) {
            log.error("Exception on getByName nameOfUser={}, message={}", nameOfUser, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    public ResponseEntity<UserDTO> create(@RequestBody UserDTO user) throws InvalidRequestException {
        try {
            return new ResponseEntity<>(userService.create(user), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            log.error("Exception on create user={}, type=DataIntegrityViolationException, message={}", user, e.getMessage());
            String codMessage =
                    Objects.requireNonNull(e.getMessage()).contains("ConstraintViolationException") ?
                            "USER_FIELDS_DUPLICATED" :
                            "DATA_INTEGRITY_VIOLATION";
            throw new InvalidRequestException(MessageUtil.getMessage(codMessage));
        } catch (Exception e) {
            log.error("Exception on create user={}, message={}", user, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    public ResponseEntity<UserDTO> update(@PathVariable(value = "id") Long userId, @RequestBody UserDTO userDTO) throws InvalidRequestException, NotFoundException {
        try {
            return ResponseEntity.ok(userService.edit(userId, userDTO));
        } catch (DataIntegrityViolationException e) {
            log.error("Exception on create user={}, type=DataIntegrityViolationException, message={}", userDTO, e.getMessage());
            String codMessage =
                    Objects.requireNonNull(e.getMessage()).contains("ConstraintViolationException") ?
                            "USER_FIELDS_DUPLICATED" :
                            "DATA_INTEGRITY_VIOLATION";
            throw new InvalidRequestException(MessageUtil.getMessage(codMessage));
        } catch (Exception e) {
            log.error("Exception on update userDTO={}, message={}", userDTO, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    public Map<String, Long> delete(@PathVariable(value = "id") Long userId) throws NotFoundException {
        try {
            userService.delete(userId);
            Map<String, Long> response = new HashMap<>();
            response.put("id", userId);
            return response;
        } catch (Exception e) {
            log.error("Exception on delete userId={}, message={}", userId, e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    public ResponseEntity<String> changeUserPassword(@RequestBody UpdatePassword updatePassword) throws InvalidRequestException {
        try {
            userService.changeUserPassword(updatePassword);
            return ResponseEntity.status(HttpStatus.OK).body("Updated");
        } catch (Exception e) {
            log.error("Exception on changeUserPassword message={}", e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }

    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        try {
            return ResponseEntity.ok().body(userService.getAllRoles());
        } catch (Exception e) {
            log.error("Exception on getAllRoles message={}", e.getMessage());
            e.setStackTrace(new StackTraceElement[0]);
            throw e;
        }
    }
}
