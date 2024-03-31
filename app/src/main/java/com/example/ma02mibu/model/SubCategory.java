package com.example.ma02mibu.model;

public class SubCategory {
    private Long id;
    private Long categoryId;
    private String name;
    private String description;
    private SUBCATEGORYTYPE type;
    public enum SUBCATEGORYTYPE { USLUGA, PROIZVOD }

    public SubCategory() {
    }

    public SubCategory(Long id, Long categoryId, String name, String description, SUBCATEGORYTYPE type) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SUBCATEGORYTYPE getType() {
        return type;
    }

    public void setType(SUBCATEGORYTYPE type) {
        this.type = type;
    }
}