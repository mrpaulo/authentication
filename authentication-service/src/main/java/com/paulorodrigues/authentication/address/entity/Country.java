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
package com.paulorodrigues.authentication.address.entity;

import com.paulorodrigues.authentication.commons.model.CountryDTO;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

import static com.paulorodrigues.authentication.commons.util.FormatUtil.removeLastComma;


/**
 *
 * @author paulo.rodrigues
 */
@Entity
@Table(indexes = {
    @Index(name = "idx_name_country", columnList = "name"),
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Country implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @SequenceGenerator(name = "SEQ_COUNTRY", allocationSize = 1, sequenceName = "country_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_COUNTRY")
    @Id
    private long id;
    
    @NotNull
    @Column(length = 100)
    private String name;

    public CountryDTO toDTO() {
        return  CountryDTO.builder()
                .id(id)
                .name(name)
                .build();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Country{");
        sb.append("id='").append(id).append('\'').append(", ");
        if (name != null && !name.isEmpty()) {
            sb.append("name='").append(name).append('\'').append(", ");
        }
        sb = removeLastComma(sb);
        sb.append('}');
        return sb.toString();
    }
}
