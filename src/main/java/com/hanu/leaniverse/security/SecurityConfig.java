package com.hanu.leaniverse.security;

import com.hanu.leaniverse.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {


    private final UserService userService;

    @Autowired
    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests(configurer -> configurer
                                .requestMatchers( "/", "/login/**", "/logout/**", "/sign-up/**", "/img/**").permitAll()
                                .requestMatchers("/quizz").permitAll()
                                .requestMatchers("/question").permitAll()

                                /// admin
//                        .requestMatchers(HttpMethod.GET,"/api/admin/**").hasAuthority("ADMIN")
//                        .requestMatchers(HttpMethod.POST,"/api/admin/**").hasAuthority("ADMIN")
                              .anyRequest().permitAll()


                )
                .formLogin(
                        form -> form.loginPage("/login")
                                .defaultSuccessUrl("/home-page", true)
                                .permitAll()
                )
                .sessionManagement(session -> session.maximumSessions(1))
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}



