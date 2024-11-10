package com.tracker.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "categories")
public class Category {
    @Id
    private String name;
    private String description;
    private String color;
}
