package ru.mars.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mars.dto.JwtAuthenticationResponse;
import ru.mars.dto.SignInRequest;
import ru.mars.dto.SignUpRequest;
import ru.mars.entities.User;
import ru.mars.enums.Role;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        var user = User.builder()
            .username(request.username())
            .password(passwordEncoder.encode(request.password()))
            .role(Role.fromString(request.role()))
            .build();
        userService.create(user);
        var jwt = jwtService.generateToken(user);

        return new JwtAuthenticationResponse(jwt);
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            request.username(),
            request.password()
        ));
        var user = userService
            .userDetailsService()
            .loadUserByUsername(request.username());
        var jwt = jwtService.generateToken(user);

        return new JwtAuthenticationResponse(jwt);
    }
}
