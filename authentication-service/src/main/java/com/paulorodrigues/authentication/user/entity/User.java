/*
 * Copyright (C) 2023 paulo.rodrigues
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
package com.paulorodrigues.authentication.user.entity;


import com.paulorodrigues.authentication.commons.exception.InvalidRequestException;
import com.paulorodrigues.authentication.commons.model.UserDTO;
import com.paulorodrigues.authentication.commons.util.ConstantsUtil;
import com.paulorodrigues.authentication.commons.util.FormatUtil;
import com.paulorodrigues.authentication.commons.util.MessageUtil;
import com.paulorodrigues.authentication.person.entity.Person;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author paulo.rodrigues
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@Table(name="lbs_user", uniqueConstraints = {
        @UniqueConstraint(name = "unique_cpf", columnNames = "cpf"),
        @UniqueConstraint(name = "unique_username", columnNames = "username")})
public class User {
    
    @Id
    @SequenceGenerator(name = "SEQ_USER", allocationSize = 1, sequenceName = "user_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USER")
    private Long id;

    @NotNull
    @Column(unique = true, length = ConstantsUtil.MAX_SIZE_NAME)
    private String username;
    
//    @JsonIgnore
    @Column(length = ConstantsUtil.MAX_SIZE_NAME)
    private String password;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "PERSON_ID", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "PERSON_USER"))
    private Person person;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="user_role",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="role_id")
    )
    private List<Role> roles;
    
    private LocalDate createAt;
    private String createBy;
    private LocalDate updateAt;
    private String updateBy;

    public User() {
    }

    public User(String name, Person person) {
        super();
        this.username = name;
        this.person = person;
    }
    public User(User user) {
        super();
        this.username = user.getUsername();
        this.person = user.getPerson();
        this.password = user.getPassword();
        this.roles = user.getRoles();
        this.id = user.getId();
    }
    public User(String name, Person person, String password, List<Role> roles) {
        super();
        this.username = name;
        this.person = person;
        this.roles = roles;
        this.password = password;
    }

    public UserDTO toDTO(){
        return UserDTO.builder()
                .id(id)
                .username(username)
                .password(password)
                .person(person.toDTO())
                .roles(roles.stream().map(Role::toDTO).collect(Collectors.toList()))
                .build();
    }

    public void validation() throws InvalidRequestException {
        if (StringUtils.isBlank(username)) {
            throw new InvalidRequestException(MessageUtil.getMessage("USERNAME_NOT_INFORMED"));
        }
        if (!FormatUtil.isEmptyOrNull(username) && username.length() > ConstantsUtil.MAX_SIZE_NAME) {
            throw new InvalidRequestException(MessageUtil.getMessage("USERNAME_OUT_OF_BOUND", ConstantsUtil.MAX_SIZE_NAME + ""));
        }
        if (StringUtils.isBlank(password)) {
            throw new InvalidRequestException(MessageUtil.getMessage("USER_PASSWORD_NOT_INFORMED"));
        }
        if (StringUtils.isNotBlank(password) && password.length() > ConstantsUtil.MAX_SIZE_NAME) {
            throw new InvalidRequestException(MessageUtil.getMessage("USER_PASSWORD_OUT_OF_BOUND", ConstantsUtil.MAX_SIZE_NAME + ""));
        }
    }

    public void persistAt() {
        if (createAt == null) {
            setCreateAt(LocalDate.now());
            setCreateBy(FormatUtil.getUsernameLogged());
        } else {
            setUpdateAt(LocalDate.now());
            setUpdateBy(FormatUtil.getUsernameLogged());
        }
    }
}
