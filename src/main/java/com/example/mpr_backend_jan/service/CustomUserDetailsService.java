package com.example.mpr_backend_jan.service;

import com.example.mpr_backend_jan.model.User;
import com.example.mpr_backend_jan.repository.UserRepository;
import com.example.mpr_backend_jan.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    public CustomUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<User> user = repo.findByEmail(email);
        return new CustomUserDetails(user.orElse(null));
    }
}
