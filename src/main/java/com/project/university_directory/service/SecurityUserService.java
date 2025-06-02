package com.project.university_directory.service;

import com.project.university_directory.model.Role;
import com.project.university_directory.model.SecurityUser;
import com.project.university_directory.repository.RoleRepository;
import com.project.university_directory.repository.SecurityUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SecurityUserService {
    private final SecurityUserRepository securityUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityUserService(SecurityUserRepository securityUserRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.securityUserRepository = securityUserRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public SecurityUser saveUser(SecurityUser user, String roleName) {
        Role studentRole = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role " + roleName + " not found"));

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of(studentRole));

        return securityUserRepository.save(user);
    }

    public void deleteUser(String email) {
        securityUserRepository.deleteById(email);
    }

    public SecurityUser findByEmail (String email) {
        return securityUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("There is no user by this email " + email));
    }

    public boolean isPasswordMatch(String currentPassword, String password) {
        return passwordEncoder.matches(currentPassword, password);
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public void save(SecurityUser currentUser) {
        securityUserRepository.save(currentUser);
    }

    public boolean isEmailExist(String email) {
        return securityUserRepository.existsById(email);
    }
}
