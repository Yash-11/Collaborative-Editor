package com.example.editor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.editor.dto.UserDTO;
import com.example.editor.models.User;
import com.example.editor.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public UserDTO fetchUserByUsername(String email, String role) {
        User user = userRepository.findOneByEmailIgnoreCase(email).get();
        return new UserDTO(user.getEmail(), user.getEmail(), user.getPassword());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionaluser = userRepository.findOneByEmailIgnoreCase(username);

        if (optionaluser.isPresent()) {
            User user = optionaluser.get();
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("ROLE_PATIENT")));
        } else {
            throw new UsernameNotFoundException("User not found");
        }        
    }
}
