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

import com.paulorodrigues.authentication.commons.model.RoleDTO;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author paulo.rodrigues
 */
@Entity
@Data
public class Role implements GrantedAuthority {
    
    public Role(String name) {
        this.name = name;
    }
    public Role() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(unique = true)
    private String name;


    @Override
    public String getAuthority() {
        return  this.name;
    }

    public RoleDTO toDTO(){
        return RoleDTO.builder()
                .id(id)
                .name(name)
                .build();
    }
}
