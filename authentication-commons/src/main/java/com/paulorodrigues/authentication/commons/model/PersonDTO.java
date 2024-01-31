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
package com.paulorodrigues.authentication.commons.model;


import com.paulorodrigues.authentication.commons.enums.Gender;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

import static com.paulorodrigues.authentication.commons.util.FormatUtil.printUpdateControl;
import static com.paulorodrigues.authentication.commons.util.FormatUtil.removeLastComma;


/**
 *
 * @author paulo.rodrigues
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PersonDTO  {

    private long id;
    private String firstName;
    private String lastName;
    private String nickName;
    private LocalDate birthdate;
    private Gender gender;
    private String cpf;
    private String email;
    private CityDTO birthCity;
    private CountryDTO birthCountry;
    private AddressDTO address;
    private String description;

    private LocalDate createAt;
    private String createBy;
    private LocalDate updateAt;
    private String updateBy;

    public String getName(){
        return String.format(firstName, " ", lastName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("PersonDTO{");
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
