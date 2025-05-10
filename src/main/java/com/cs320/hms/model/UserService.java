package com.cs320.hms.service;

import com.cs320.hms.model.User;
import com.cs320.hms.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public User create(User user) {
        return repo.save(user);
    }

    public User getBySsn(String ssn) {
        return repo.findById(ssn).orElse(null);
    }

    public boolean deleteBySsn(String ssn) {
        if (!repo.existsById(ssn)) return false;
        repo.deleteById(ssn);
        return true;
    }

    public List<User> getByRole(String role) {
        return repo.findByUserRole(role);
    }

    public User authenticate(String ssn, String password) {
        return repo.findBySsnAndPassword(ssn, password).orElse(null);
    }

    public User update(User user) {
        return repo.save(user);
    }
}
