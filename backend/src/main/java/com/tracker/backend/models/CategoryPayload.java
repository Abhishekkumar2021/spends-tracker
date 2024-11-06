package com.tracker.backend.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryPayload {
    @NotBlank(message = "Category name is required")
    @Size(min = 3, max = 50, message = "Category name must be between 3 and 50 characters")
    private String name;

    @Size(max = 255, message = "Category description must be less than 255 characters")
    private String description;
    private String color;

    public CategoryPayload() {
        this.description = "";
        this.color = "#000000";
    }
}
