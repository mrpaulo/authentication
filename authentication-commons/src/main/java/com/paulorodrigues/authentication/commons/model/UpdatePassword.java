package com.paulorodrigues.authentication.commons.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author paulo.rodrigues
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePassword {

    private String currentPassword;
    private String newPassword;


}
