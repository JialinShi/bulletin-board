package com.jialin.BulletinBoard.controller;

import com.jialin.BulletinBoard.models.AuthenticationResponse;
import com.jialin.BulletinBoard.models.User;
import com.jialin.BulletinBoard.security.JwtUtil;
import com.jialin.BulletinBoard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        final UserDetails userDetails = userService.loadUserByUsername(loginRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        User user = userService.findByEmail(loginRequest.getEmail());

        // remove password from entity to avoid information breach
        user.setPassword(null);

        return ResponseEntity.ok(new AuthenticationResponse(jwt, user));
    }
}
