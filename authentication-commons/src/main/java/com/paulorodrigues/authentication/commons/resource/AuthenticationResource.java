package com.paulorodrigues.authentication.commons.resource;

import com.paulorodrigues.authentication.commons.model.Login;
import com.paulorodrigues.authentication.commons.model.UserDTO;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/api/v1/authentications")
public interface AuthenticationResource {

    @ResponseBody
    @Secured({Login.ROLE_CLIENT, Login.ROLE_ADMIN})
    @GetMapping("/user-auth")
    UserDTO user();

    @GetMapping("/logout")
    boolean logout(HttpServletRequest request);
}
