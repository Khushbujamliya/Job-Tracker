package com.tracker.service;

import com.tracker.dto.*;
import com.tracker.model.User;
import com.tracker.repository.UserRepository;
import com.tracker.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        JwtUtil jwtUtil,
                        AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("An account with this email already exists");
        }

        // Never store the raw password — always hash it first
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getEmail(), request.getName(), hashedPassword);
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getName(), user.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        // This throws an exception automatically if credentials are invalid
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getName(), user.getEmail());
    }
}
