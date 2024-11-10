package com.tracker.backend.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.tracker.backend.models.Category;
import reactor.core.publisher.Mono;

public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {
    Mono<Boolean> existsByName(String name);
}
