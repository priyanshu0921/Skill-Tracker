package com.skilltracker.service;

import com.skilltracker.dto.AuthRequest;
import com.skilltracker.dto.AuthResponse;
import com.skilltracker.dto.RegisterRequest;
import com.skilltracker.dto.UserProfileResponse;
import com.skilltracker.model.AppUser;
import com.skilltracker.model.Role;
import com.skilltracker.repository.AppUserRepository;
import com.skilltracker.security.JwtService;
import java.time.Instant;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            AppUserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new IllegalArgumentException("An account with this email already exists");
        }

        AppUser user = userRepository.save(AppUser.builder()
                .fullName(request.fullName())
                .email(request.email().toLowerCase())
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .createdAt(Instant.now())
                .build());

        return buildAuthResponse(user);
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        AppUser user = userRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return buildAuthResponse(user);
    }

    public UserProfileResponse getProfile(AppUser user) {
        return new UserProfileResponse(user.getId(), user.getFullName(), user.getEmail());
    }

    private AuthResponse buildAuthResponse(AppUser user) {
        return new AuthResponse(jwtService.generateToken(user.getEmail()), getProfile(user));
    }
}
