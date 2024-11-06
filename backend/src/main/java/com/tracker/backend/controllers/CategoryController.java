package com.tracker.backend.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.tracker.backend.exceptions.ServiceException;
import com.tracker.backend.models.CategoryPayload;
import com.tracker.backend.models.CustomResponse;
import com.tracker.backend.services.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CustomResponse> createCategory(@RequestBody @Valid CategoryPayload categoryPayload)
            throws ServiceException {
        return categoryService.createCategory(categoryPayload);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<CustomResponse> getCategories() throws ServiceException {
        return categoryService.getCategories();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<CustomResponse> getCategoryById(@PathVariable String id) throws ServiceException {
        return categoryService.getCategoryById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<CustomResponse> updateCategoryById(@RequestBody @Valid CategoryPayload categoryPayload,
            @PathVariable String id) throws ServiceException {
        return categoryService.updateCategory(id, categoryPayload);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<CustomResponse> deleteCategoryById(@PathVariable String id) throws ServiceException {
        return categoryService.deleteCategory(id);
    }
}
