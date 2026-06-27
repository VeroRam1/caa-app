package com.caa.backend.security;


import com.caa.backend.model.Tutor;
import com.caa.backend.repository.TutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Spring Security service that loads a Tutor from the database by email.
 * The email is used as the username throughout the authentication process.
 *
 * This bridges our Tutor entity with Spring Security's UserDetails interface.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{
    private final TutorRepository tutorRepository;


    // Loads a tutor by email for authentication.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Tutor tutor = tutorRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "No tutor found with email: " + email
                ));

        return new org.springframework.security.core.userdetails.User(
                tutor.getEmail(),
                tutor.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + tutor.getRole().name()))
        );
    }
}
