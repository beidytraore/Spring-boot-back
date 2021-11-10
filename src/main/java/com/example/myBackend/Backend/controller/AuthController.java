package com.example.myBackend.Backend.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.myBackend.Backend.dao.RoleRepository;
import com.example.myBackend.Backend.dao.UserRepository;
import com.example.myBackend.Backend.model.*;
import com.example.myBackend.Backend.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@Valid @RequestBody User user) {
        //System.out.println("AuthController -- userLogin");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenUtil.generateJwtToken(authentication);
        CustomUserBean userBean = (CustomUserBean) authentication.getPrincipal();
        List<String> roles = userBean.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.toList());
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        authResponse.setRoles(roles);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> userSignup(@Valid @RequestBody SignupRequest signupRequest) {
        if(userRepository.existsByUserName(signupRequest.getUserName())){
            return ResponseEntity.badRequest().body("Username is already taken");
        }
        if(userRepository.existsByEmail(signupRequest.getEmail())){
            return ResponseEntity.badRequest().body("Email is already taken");
        }
        User user = new User();
        Set<Role> roles = new HashSet<>();
        user.setUserName(signupRequest.getUserName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(encoder.encode(signupRequest.getPassword()));
        //System.out.println("Encoded password--- " + user.getPassword());
        String[] roleArr = signupRequest.getRoles();

        if(roleArr == null) {
            roles.add(roleRepository.findByRoleName(Roles.ROLE_USER).get());
        }
        for(String role: roleArr) {
            switch(role) {
                case "admin":
                    roles.add(roleRepository.findByRoleName(Roles.ROLE_ADMIN).get());
                    break;
                case "user":
                    roles.add(roleRepository.findByRoleName(Roles.ROLE_USER).get());
                    break;
                default:
                    return ResponseEntity.badRequest().body("Specified role not found");
            }
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok("User signed up successfully");
    }
}