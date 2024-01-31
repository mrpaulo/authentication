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


import com.paulorodrigues.authentication.commons.model.CityDTO;
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
    @Index(name = "idx_name_city", columnList = "name"),
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class City implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @SequenceGenerator(name = "SEQ_CITY", allocationSize = 1, sequenceName = "city_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CITY")
    @Id
    private long id;
    
    @NotNull
    @Column(length = 100)
    private String name;
        
    @NotNull
    @OneToOne
    @JoinColumn(name = "STATE_ID", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "STATE_CITY"))
    private StateCountry state;
    
    @NotNull
    @OneToOne
    @JoinColumn(name = "COUNTRY_ID", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "COUNTRY_CITY"))
    private Country country;
    
    @Column(length = 10)
    private String ibgeCode;

    public CityDTO toDTO() {
        return CityDTO.builder()
                .id(id)
                .name(name)
                .build();
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("City{");
        sb.append("id='").append(id).append('\'').append(", ");
        if (name != null && !name.isEmpty()) {
            sb.append("name='").append(name).append('\'').append(", ");
        }
        if (state != null) {
            sb.append("state='").append(state).append('\'').append(", ");
        }
        if (country != null) {
            sb.append("country='").append(country).append('\'').append(", ");
        }
        if (ibgeCode != null && !ibgeCode.isEmpty()) {
            sb.append("ibgeCode='").append(ibgeCode).append('\'').append(", ");
        }
        sb = removeLastComma(sb);
        sb.append('}');
        return sb.toString();
    }


}
