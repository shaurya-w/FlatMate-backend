package com.example.mpr_backend_jan.controller;

import com.example.mpr_backend_jan.dto.LoginRequest;
import com.example.mpr_backend_jan.model.PasswordResetToken;
import com.example.mpr_backend_jan.model.User;
import com.example.mpr_backend_jan.repository.PasswordResetTokenRepository;
import com.example.mpr_backend_jan.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")        // ← only this on the class, nothing else
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetTokenRepository resetTokenRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.frontend.url}")
    private String frontendUrl;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpSession session) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                );

        SecurityContextHolder.getContext().setAuthentication(authToken);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        return ResponseEntity.ok("Login successful");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out");
    }

    @PostMapping("/forgot-password")
    @Transactional // ← /auth/forgot-password (prefix comes from class)
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");

        userRepository.findByEmail(email).ifPresent(user -> {
            resetTokenRepository.deleteByEmail(email);

            PasswordResetToken token = new PasswordResetToken();
            token.setToken(UUID.randomUUID().toString());
            token.setEmail(email);
            token.setExpiresAt(LocalDateTime.now().plusMinutes(30));
            resetTokenRepository.save(token);

            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(email);
            mail.setSubject("FlatMate — Reset your password");
            mail.setText("Click the link below to reset your password:\n\n"
                    + frontendUrl + "/reset-password?token=" + token.getToken()
                    + "\n\nThis link expires in 30 minutes.");
            mailSender.send(mail);
        });

        return ResponseEntity.ok("If that email exists, a reset link has been sent.");
    }

    @PostMapping("/reset-password")        // ← /auth/reset-password (prefix comes from class)
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String newPassword = body.get("password");

        PasswordResetToken resetToken = resetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            resetTokenRepository.delete(resetToken);
            return ResponseEntity.badRequest().body("Token has expired");
        }

        User user = userRepository.findByEmail(resetToken.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetTokenRepository.delete(resetToken);
        return ResponseEntity.ok("Password reset successful");
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        return ResponseEntity.ok(Map.of(
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRole().name()
        ));
    }
}