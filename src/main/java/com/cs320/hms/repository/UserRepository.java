package com.cs320.hms.repository;

import com.cs320.hms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByUserRole(String role);
    Optional<User> findBySsnAndPassword(String ssn, String password);
}
