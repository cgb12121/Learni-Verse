package com.hanu.leaniverse.service;
import com.hanu.leaniverse.dto.UserDTO;
import com.hanu.leaniverse.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User signUpAccount (UserDTO userDTO);

    User getCurrentUser(Authentication authentication);
}
