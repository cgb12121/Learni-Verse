package com.hanu.leaniverse.service;


import com.hanu.leaniverse.dto.UserDTO;
import com.hanu.leaniverse.model.Tutor;
import com.hanu.leaniverse.model.User;
import com.hanu.leaniverse.model.UserSensitiveInformation;
import com.hanu.leaniverse.repository.TutorRepository;
import com.hanu.leaniverse.repository.UserRepository;
import com.hanu.leaniverse.repository.UserSensitiveInformationRepository;
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
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TutorRepository tutorRepository;
    @Autowired
    private UserSensitiveInformationRepository userSensitiveInformationRepository;
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
    public void updateUserProfile(UserSensitiveInformation userInfo, String fullName, Authentication authentication) {
        // Get the current authenticated user
        User currentUser = getCurrentUser(authentication);
        if (currentUser == null) {
            throw new IllegalStateException("User not found");
        }

        // Check if UserSensitiveInformation exists
        Optional<UserSensitiveInformation> existingUserInfo = userSensitiveInformationRepository.findByUser(currentUser);

        if (existingUserInfo.isPresent()) {
            UserSensitiveInformation existingInfo = existingUserInfo.get();
            existingInfo.setEmail(userInfo.getEmail());
            existingInfo.setDob(userInfo.getDob());
            existingInfo.setPob(userInfo.getPob());
            existingInfo.setPhoneNumber(userInfo.getPhoneNumber());
            existingInfo.setGender(userInfo.getGender());
            existingInfo.setPosition(userInfo.getPosition());
            existingInfo.setOrganization(userInfo.getOrganization());

            userSensitiveInformationRepository.save(existingInfo);
        } else {
            userInfo.setUser(currentUser);
            userSensitiveInformationRepository.save(userInfo);
        }

        // Update user's name
        currentUser.setFullName(fullName);
        userRepository.save(currentUser);
    }
}
