package com.tracker.backend.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.tracker.backend.exceptions.ServiceException;
import com.tracker.backend.models.Category;
import com.tracker.backend.models.CategoryPayload;
import com.tracker.backend.models.CustomResponse;
import com.tracker.backend.repositories.CategoryRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final LoggingService loggingService;
    private static  final String CATEGORY = "Category";
    private static final String CATEGORY_NOT_FOUND = "Category with ID %s not found";
    private static final String CATEGORY_EXISTS = "Category with name %s already exists";



    // Create a new category
    public Mono<CustomResponse> createCategory(CategoryPayload categoryPayload) throws ServiceException {
        Category category = new Category();
        category.setName(categoryPayload.getName());
        category.setDescription(categoryPayload.getDescription());
        category.setColor(categoryPayload.getColor());

        return categoryRepository.existsByName(category.getName())
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(
                                new ServiceException(String.format(CATEGORY_EXISTS, category.getName()),
                                        HttpStatus.CONFLICT));
                    }

                    return categoryRepository.save(category);
                })
                .map(savedCategory -> {
                    CustomResponse response = new CustomResponse();
                    response.setMessage("Category created successfully");
                    response.addData(CATEGORY, savedCategory);
                    response.setStatusCode(HttpStatus.CREATED.value());
                    return response;
                })
                .doOnSuccess(success -> loggingService.info("Category created successfully"))
                .doOnError(error -> loggingService.error(error.getMessage()));
    }

    // Get all categories
    public Mono<CustomResponse> getCategories() {
        return categoryRepository.findAll()
                .collectList()
                .map(categories -> {
                    CustomResponse response = new CustomResponse();
                    response.addData("categories", categories);
                    response.setMessage("Categories retrieved successfully");
                    response.setStatusCode(HttpStatus.OK.value());
                    return response;
                })
                .doOnSuccess(success -> loggingService.info("Categories retrieved successfully"))
                .doOnError(error -> loggingService.error(error.getMessage()));
    }

    // Get a category by ID
    public Mono<CustomResponse> getCategoryById(String id) throws ServiceException {
        return categoryRepository.findById(id)
                .switchIfEmpty(
                        Mono.error(new ServiceException(String.format(CATEGORY_NOT_FOUND, id), HttpStatus.NOT_FOUND)))
                .flatMap(category -> {
                    CustomResponse response = new CustomResponse();
                    response.addData(CATEGORY, category);
                    response.setMessage("Category retrieved successfully");
                    response.setStatusCode(HttpStatus.OK.value());
                    return Mono.just(response);
                })
                .doOnSuccess(success -> loggingService.info("Category retrieved successfully"))
                .doOnError(error -> loggingService.error(error.getMessage()));
    }

    // Update a category
    public Mono<CustomResponse> updateCategory(String id, CategoryPayload categoryPayload) throws ServiceException {
        return categoryRepository.findById(id)
                .switchIfEmpty(
                        Mono.error(new ServiceException(String.format(CATEGORY_NOT_FOUND, id), HttpStatus.NOT_FOUND)))
                .flatMap(category -> {
                    category.setName(categoryPayload.getName());
                    category.setDescription(categoryPayload.getDescription());
                    category.setColor(categoryPayload.getColor());

                    return categoryRepository.save(category);
                })
                .map(updatedCategory -> {
                    CustomResponse response = new CustomResponse();
                    response.addData("category", updatedCategory);
                    response.setMessage("Category updated successfully");
                    response.setStatusCode(HttpStatus.OK.value());
                    return response;
                })
                .doOnSuccess(success -> loggingService.info("Category updated successfully"))
                .doOnError(error -> loggingService.error(error.getMessage()));
    }

    // Delete a category
    public Mono<CustomResponse> deleteCategory(String id) throws ServiceException {
        return categoryRepository.findById(id)
                .switchIfEmpty(
                        Mono.error(new ServiceException(String.format(CATEGORY_NOT_FOUND, id), HttpStatus.NOT_FOUND)))
                .flatMap(category -> categoryRepository.delete(category).then(Mono.just(category)))
                .map(deletedCategory -> {
                    CustomResponse response = new CustomResponse();
                    response.addData("category", deletedCategory);
                    response.setMessage("Category deleted successfully");
                    response.setStatusCode(HttpStatus.OK.value());
                    return response;
                })
                .doOnSuccess(success -> loggingService.info("Category deleted successfully"))
                .doOnError(error -> loggingService.error(error.getMessage()));
    }
}
