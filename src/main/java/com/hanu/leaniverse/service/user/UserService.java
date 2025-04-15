package com.hanu.leaniverse.service.user;
import com.hanu.leaniverse.dto.UserDTO;
import com.hanu.leaniverse.model.User;
import com.hanu.leaniverse.model.UserSensitiveInformation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User signUpAccount (UserDTO userDTO);
    User getCurrentUser(Authentication authentication);
    void updateUserProfile(UserSensitiveInformation userInfo, String fullName, Authentication authentication);
}
