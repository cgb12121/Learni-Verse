package com.hanu.leaniverse.service;


import com.hanu.leaniverse.dto.UserDTO;
import com.hanu.leaniverse.model.Tutor;
import com.hanu.leaniverse.model.User;
import com.hanu.leaniverse.repository.TutorRepository;
import com.hanu.leaniverse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TutorRepository tutorRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Wrong username or password.");
        }

        boolean isTutor = tutorRepository.existsByUser(user);


        Collection<? extends GrantedAuthority> authorities;
        if (isTutor) {
            authorities = rolesToAuthorities("TUTOR");
        } else {
            authorities = rolesToAuthorities("STUDENT");
        }


        System.out.println("UserDetails: " + user.getUsername() + " - Role: " + (isTutor ? "TUTOR" : "STUDENT"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }

    private Collection<? extends GrantedAuthority> rolesToAuthorities(String role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        return authorities;
    }

    @Override
    public User signUpAccount(UserDTO userDTO) {

        System.out.println("Signing up user with role: " + userDTO.getRole());

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setFullName(userDTO.getFullName());
        user.setPassword("{noop}" + userDTO.getPassword());


        User savedUser = userRepository.save(user);


        if ("TUTOR".equals(userDTO.getRole())) {
            Tutor tutor = new Tutor();
            tutor.setUser(savedUser);

            tutorRepository.save(tutor);
        }

        return savedUser;
    }
    public User getCurrentUser(Authentication authentication){
        if(authentication !=null && authentication.getPrincipal() instanceof UserDetails){
            String username =  ((UserDetails) authentication.getPrincipal()).getUsername();
            return userRepository.findByUsername(username);
        }
        return null;
    }

}
