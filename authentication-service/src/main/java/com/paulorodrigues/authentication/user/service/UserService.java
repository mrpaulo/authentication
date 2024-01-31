package com.paulorodrigues.authentication.user.service;


import com.paulorodrigues.authentication.commons.exception.InvalidRequestException;
import com.paulorodrigues.authentication.commons.exception.NotFoundException;
import com.paulorodrigues.authentication.commons.model.*;
import com.paulorodrigues.authentication.commons.util.FormatUtil;
import com.paulorodrigues.authentication.commons.util.MessageUtil;
import com.paulorodrigues.authentication.person.service.PersonService;
import com.paulorodrigues.authentication.user.entity.Role;
import com.paulorodrigues.authentication.user.entity.User;
import com.paulorodrigues.authentication.user.repository.RoleRepository;
import com.paulorodrigues.authentication.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author paulo.rodrigues
 */
@Service
@Transactional
@Log4j2
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    PersonService personService;


    public Page<User> findPageable(UserQuery filter, Pageable pageable) {
        log.info("Finding pageable users by filter={}", filter);
        return userRepository.findPageable(
                filter.getId(),
                filter.getUsername(),
                filter.getStartDate(),
                filter.getEndDate(),
                pageable);
    }

    public User findById(Long userId) throws NotFoundException {
        log.info("Finding user by userId={}", userId);
        Optional<User> user = userRepository.findById(userId);
        if (user == null || !user.isPresent()) {
            log.error("User not found by userId={}", userId);
            throw new NotFoundException(MessageUtil.getMessage("USER_NOT_FOUND") + " ID: " + userId);
        }
        return user.get();
    }

    public UserDTO findByIdDTO(Long userId) throws NotFoundException {
        return findById(userId).toDTO();
    }

    public List<UserDTO> findByName(String name) {
        log.info("Finding user by name={}", name);
        return usersToDTOs(userRepository.findByName(name));
    }

    public UserDTO create(UserDTO userDTO) throws InvalidRequestException {
        assert userDTO != null : MessageUtil.getMessage("USER_IS_NULL");
        User user = transformToUserFromDTO(userDTO);
        if (user.getRoles().isEmpty()) {
            Role role = roleRepository.findByName(Login.ROLE_CLIENT);
            if (role == null) {
                role = new Role(Login.ROLE_CLIENT);
                role = roleRepository.save(role);
            }

            user.setRoles(List.of(role));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("Creating user username={}", user.getUsername());
        return save(user).toDTO().hidePassword();
    }

    public User save(User user) throws InvalidRequestException {
        user.validation();
        user.persistAt();
        log.info("Saving id={}, userName={}", user.getId(), user.getUsername());
        return userRepository.saveAndFlush(user);
    }

    public UserDTO edit(Long userId, UserDTO userDetail) throws InvalidRequestException, NotFoundException {
        User userToEdit = findById(userId);
        String pw = userToEdit.getPassword();
        LocalDate createAt = userToEdit.getCreateAt();
        String createBy = userToEdit.getCreateBy();

        ModelMapper mapper = new ModelMapper();
        userToEdit = mapper.map(userDetail, User.class);
        userToEdit.setCreateAt(createAt);
        userToEdit.setCreateBy(createBy);
        userToEdit.setPassword(pw);
        log.info("Updating user id={}, userName={}", userToEdit.getId(), userToEdit.getUsername());
        return save(userToEdit).toDTO();
    }

    public void delete(Long userId) throws NotFoundException {
        User userToDelete = findById(userId);
        log.info("Deleting id={}, userName={}", userToDelete.getId(), userToDelete.getUsername());
        userRepository.delete(userToDelete);
    }

    public User getUserFromDTO(UserDTO dto){
        try {
            return Objects.nonNull(dto) ? findById(dto.getId()) : null;
        } catch (Exception e) {
            log.error("Exception on getUserFromDTO userDTO={}, message={}", dto, e.getMessage());
            return null;
        }
    }
    public User transformToUserFromDTO(UserDTO dto) {
        return User.builder()
                .id(dto.getId())
                .password(dto.getPassword())
                .username(dto.getUsername())
                .person(personService.personFromDTO(dto.getPerson()))
                .build();
    }

    public List<UserDTO> usersToDTOs(List<User> users) {
        return users.stream().map(user -> user.toDTO().hidePassword()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream().map(Role::toDTO).collect(Collectors.toList());
    }

    public void changeUserPassword(UpdatePassword updatePassword) throws InvalidRequestException {
        User user = userRepository.findByEmail(FormatUtil.getUsernameLogged());
        if (!checkIfValidOldPassword(user, updatePassword.getCurrentPassword())) {
            throw new InvalidRequestException(MessageUtil.getMessage("INCORRECT_PASSWORD"));
        }
        user.setPassword(passwordEncoder.encode(updatePassword.getNewPassword()));
        userRepository.save(user);
    }

    private boolean checkIfValidOldPassword(final User user, final String oldPassword) {
        return user != null && passwordEncoder.matches(oldPassword, user.getPassword());
    }
}