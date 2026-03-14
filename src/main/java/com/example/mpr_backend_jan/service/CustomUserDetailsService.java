package com.example.mpr_backend_jan.service;

import com.example.mpr_backend_jan.model.User;
import com.example.mpr_backend_jan.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    public CustomUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Attempting login for email: " + email);

        Optional<User> userOptional = repo.findByEmail(email);
        if (userOptional.isEmpty()) {
            System.out.println("User not found: " + email);
            throw new UsernameNotFoundException("User not found: " + email);
        }

        User user = userOptional.get();
        System.out.println("User found: " + user.getEmail() + ", role: " + user.getRole());

        // Map roles to Spring Security format (must start with ROLE_)
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole())
        );

        // Return Spring Security UserDetails
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}