/*
 * Copyright (C) 2023 paulo.rodrigues
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
package com.paulorodrigues.authentication.person.entity;

import com.paulorodrigues.authentication.address.entity.Address;
import com.paulorodrigues.authentication.address.entity.City;
import com.paulorodrigues.authentication.address.entity.Country;
import com.paulorodrigues.authentication.commons.enums.Gender;
import com.paulorodrigues.authentication.commons.exception.InvalidRequestException;
import com.paulorodrigues.authentication.commons.model.PersonDTO;
import com.paulorodrigues.authentication.commons.util.ConstantsUtil;
import com.paulorodrigues.authentication.commons.util.FormatUtil;
import com.paulorodrigues.authentication.commons.util.MessageUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import static com.paulorodrigues.authentication.commons.util.FormatUtil.printUpdateControl;
import static com.paulorodrigues.authentication.commons.util.FormatUtil.removeLastComma;


/**
 *
 * @author paulo.rodrigues
 */
@Entity
@Table(
        uniqueConstraints = {
            @UniqueConstraint(name = "unique_cpf", columnNames = "cpf")},
        indexes = {
            @Index(name = "idx_first_name_person", columnList = "firstName")
})
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Person implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @SequenceGenerator(name = "SEQ_PERSON", allocationSize = 1, sequenceName = "person_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PERSON")
    @Id
    private long id;

    @Column(length = ConstantsUtil.MAX_SIZE_NAME, nullable = false)
    private String firstName;

    @Column(length = ConstantsUtil.MAX_SIZE_NAME, nullable = false)
    private String lastName;

    @Column(length = ConstantsUtil.MAX_SIZE_NAME)
    private String nickName;

    private LocalDate birthdate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(length = ConstantsUtil.MAX_SIZE_NAME)
    private String email;

    @Column(unique = true, length = ConstantsUtil.MAX_SIZE_CPF)
    private String cpf;

    @ManyToOne
    @JoinColumn(name = "BIRTH_CITY_ID", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "BIRTH_CITY_PERSON"))
    private City birthCity;

    @ManyToOne
    @JoinColumn(name = "BIRTH_COUNTRY_ID", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "BIRTH_COUNTRY_PERSON"))
    private Country birthCountry;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "ADDRESS_PERSON"))
    private Address address;
    
    @Column(length = ConstantsUtil.MAX_SIZE_LONG_TEXT)
    private String description;
        
    private LocalDate createAt;
    private String createBy;
    private LocalDate updateAt;
    private String updateBy;

    public void validation() throws InvalidRequestException {
        if (StringUtils.isBlank(firstName)) {
            throw new InvalidRequestException(MessageUtil.getMessage("PERSON_FIRST_NAME_NOT_INFORMED"));
        }
        if (StringUtils.isBlank(lastName)) {
            throw new InvalidRequestException(MessageUtil.getMessage("PERSON_LAST_NAME_NOT_INFORMED"));
        }
        if (firstName.length() > ConstantsUtil.MAX_SIZE_NAME) {
            throw new InvalidRequestException(MessageUtil.getMessage("PERSON_FIRST_NAME_OUT_OF_BOUND", ConstantsUtil.MAX_SIZE_NAME + ""));
        }
        if (lastName.length() > ConstantsUtil.MAX_SIZE_NAME) {
            throw new InvalidRequestException(MessageUtil.getMessage("PERSON_LAST_NAME_OUT_OF_BOUND", ConstantsUtil.MAX_SIZE_NAME + ""));
        }
        if (StringUtils.isNotBlank(description) && description.length() > ConstantsUtil.MAX_SIZE_LONG_TEXT) {
            throw new InvalidRequestException(MessageUtil.getMessage("PERSON_DESCRIPTION_OUT_OF_BOUND", ConstantsUtil.MAX_SIZE_LONG_TEXT + ""));
        }
        if (StringUtils.isNotBlank(nickName) && nickName.length() > ConstantsUtil.MAX_SIZE_NAME) {
            throw new InvalidRequestException(MessageUtil.getMessage("PERSON_DESCRIPTION_OUT_OF_BOUND", ConstantsUtil.MAX_SIZE_NAME + ""));
        }
        if (StringUtils.isNotBlank(email) && email.length() > ConstantsUtil.MAX_SIZE_NAME) {
            throw new InvalidRequestException(MessageUtil.getMessage("PERSON_EMAIL_OUT_OF_BOUND", ConstantsUtil.MAX_SIZE_NAME + ""));
        }
        setCpf(FormatUtil.removeFormatCPF(cpf));
        if (StringUtils.isNotBlank(cpf) && !FormatUtil.isCPF(cpf)) {
            throw new InvalidRequestException(MessageUtil.getMessage("PERSON_CPF_INVALID"));
        }
        if(Objects.nonNull(birthdate) && birthdate.isAfter(LocalDate.now())){
            throw new InvalidRequestException(MessageUtil.getMessage("PERSON_BIRTHDATE_INVALID"));
        }
    }

    public String getName(){
        return String.format(firstName, " ", lastName);
    }

    public PersonDTO toDTO(){
        return PersonDTO.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .nickName(nickName)
                .email(email)
                .cpf(cpf)
                .gender(gender)
                .address(address.toDTO())
                .birthdate(birthdate)
                .birthCity(birthCity.toDTO())
                .birthCountry(birthCountry.toDTO())
                .build();
    }

    public void persistAt() {
        if (createBy == null) {
            setCreateAt(LocalDate.now());
            setCreateBy(FormatUtil.getUsernameLogged());
        } else {
            setUpdateAt(LocalDate.now());
            setUpdateBy(FormatUtil.getUsernameLogged());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Person{");
        sb.append("id=").append(id).append(", ");
        if (StringUtils.isNotBlank(firstName)) {
            sb.append("firstName='").append(firstName).append('\'').append(", ");
        }
        if (StringUtils.isNotBlank(lastName)) {
            sb.append("lastName='").append(lastName).append('\'').append(", ");
        }
        if (StringUtils.isNotBlank(nickName)) {
            sb.append("nickName='").append(nickName).append('\'').append(", ");
        }
        if (Objects.nonNull(birthdate)) {
            sb.append("birthdate='").append(birthdate).append('\'').append(", ");
        }
        if (Objects.nonNull(gender)) {
            sb.append("gender=").append(gender).append(", ");
        }
        if (StringUtils.isNotBlank(cpf)) {
            sb.append("cpf='").append(cpf).append('\'').append(", ");
        }
        if (StringUtils.isNotBlank(email)) {
            sb.append("email='").append(email).append('\'').append(", ");
        }
        if (Objects.nonNull(birthCity)) {
            sb.append("birthCity='").append(birthCity).append('\'').append(", ");
        }
        if (Objects.nonNull(birthCountry)) {
            sb.append("birthCountry='").append(birthCountry).append('\'').append(", ");
        }
        if (Objects.nonNull(address)) {
            sb.append("address='").append(address).append('\'').append(", ");
        }
        if (StringUtils.isNotBlank(description)) {
            sb.append("description='").append(description).append('\'').append(", ");
        }
        sb = printUpdateControl(sb, createAt, createBy, updateAt, updateBy);
        removeLastComma(sb);
        sb.append('}');
        return sb.toString();
    }
}
