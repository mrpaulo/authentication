package com.paulorodrigues.authentication.commons.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *
 * @author paulo.rodrigues
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private long id;
    private String username;
    private String password;
    private PersonDTO person;
    private List<RoleDTO> roles;

    public UserDTO hidePassword(){
        setPassword(null);
        return this;
    }
}
