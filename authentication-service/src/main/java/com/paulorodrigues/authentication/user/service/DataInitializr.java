package com.paulorodrigues.authentication.user.service;


import com.paulorodrigues.authentication.person.entity.Person;
import com.paulorodrigues.authentication.user.entity.Role;
import com.paulorodrigues.authentication.user.entity.User;
import com.paulorodrigues.authentication.user.repository.RoleRepository;
import com.paulorodrigues.authentication.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 *
 * @author paulo.rodrigues
 */
public class DataInitializr implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent arg0) {

        List<User> users = userRepository.findAll();

//        if (users.isEmpty()) {
//            createUser("Admin", "admin", passwordEncoder.encode("admin"), Login.ROLE_ADMIN);
//            createUser("Client", "client", passwordEncoder.encode("12345"), Login.ROLE_CLIENT);
//        }

    }

    public void createUser(String name, Person person, String password, String roleName) {

        Role role = new Role(roleName);

        this.roleRepository.save(role);
        User user = new User(name, person, password, List.of(role));
        userRepository.save(user);
    }

}
