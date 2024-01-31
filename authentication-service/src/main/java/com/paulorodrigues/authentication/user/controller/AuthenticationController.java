package com.paulorodrigues.authentication.user.controller;


import com.paulorodrigues.authentication.commons.model.UserDTO;
import com.paulorodrigues.authentication.commons.resource.AuthenticationResource;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author paulo.rodrigues
 */
@RestController
@CrossOrigin(origins = {"*"})
@Log4j2
public class AuthenticationController implements AuthenticationResource {

    @Resource(name = "tokenServices")
    ConsumerTokenServices tokenServices;

    public boolean logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String tokenValue = authHeader.replace("Bearer", "").trim();
            log.info("Logout token={}", tokenValue);
            tokenServices.revokeToken(tokenValue);
        }
        return true;
    }

    public UserDTO user() {
        return (UserDTO) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
