package com.project.university_directory.repository;

import com.project.university_directory.model.SecurityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecurityUserRepository extends JpaRepository<SecurityUser, String> {
    public Optional<SecurityUser> findByEmail(String email);
}
