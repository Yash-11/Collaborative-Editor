package com.example.editor.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.editor.models.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    // Custom query methods can be added here if needed
    Optional<User> findOneByEmailIgnoreCase(String email);
}

