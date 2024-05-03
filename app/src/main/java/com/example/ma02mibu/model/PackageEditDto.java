package com.example.ma02mibu.model;

public class PackageEditDto {
    private String name;
    private String description;
    private boolean availableToBuy;
    private boolean visible;
    private String firestoreId;
    private String category;

    public PackageEditDto(){
        name="";
        description="";
    }
    public PackageEditDto(String name, String description, boolean availableToBuy, boolean visible, String firestoreId, String category) {
        this.name = name;
        this.description = description;
        this.availableToBuy = availableToBuy;
        this.visible = visible;
        this.firestoreId = firestoreId;
        this.category = category;
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

    public boolean isAvailableToBuy() {
        return availableToBuy;
    }

    public void setAvailableToBuy(boolean availableToBuy) {
        this.availableToBuy = availableToBuy;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getFirestoreId() {
        return firestoreId;
    }

    public void setFirestoreId(String firestoreId) {
        this.firestoreId = firestoreId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
