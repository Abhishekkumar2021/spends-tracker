package com.tracker.backend.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.tracker.backend.models.User;

import reactor.core.publisher.Mono;


public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findByUsername(String username);
    Mono<Boolean> existsByUsername(String username);
    Mono<Boolean> existsByEmail(String email);
}