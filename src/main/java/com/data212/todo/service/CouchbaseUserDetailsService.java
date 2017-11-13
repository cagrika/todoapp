package com.data212.todo.service;

import com.data212.todo.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@RequiredArgsConstructor
public class CouchbaseUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String name) {
        User document = userService.findByName(name);
        if(document!=null) {
            String username = document.getName();
            String password = document.getPassword();
            String[] authorities = document.getRoles();
            return new CouchbaseUserDetails(
                    username,password,authorities);
        }
        return null;
    }
}