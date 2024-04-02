package com.example.ma02mibu.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Product implements Parcelable {
    private Long id;
    private String name;
    private String description;
    private String category;
    private String subCategory;
    private String price;
    private int priceLowering;
    private ArrayList<Integer> images;
    private ArrayList<String> eventTypes;
    private int currentImageIndex;
    public Product(Long id, String name, String description, String category, String subCategory, String price, ArrayList<Integer> images, ArrayList<String> eventTypes, int priceLowering) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.subCategory = subCategory;
        this.price = price;
        this.images = images;
        this.eventTypes = eventTypes;
        this.currentImageIndex = 0;
        this.priceLowering = priceLowering;
    }

    public Product() {
        this.currentImageIndex = 0;
    }
    protected Product(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        category = in.readString();
        subCategory = in.readString();
        images = in.readArrayList(null);
        eventTypes = in.readArrayList(null);
        this.currentImageIndex = 0;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ArrayList<Integer> getImage() {
        return images;
    }

    public int getPriceLowering() {
        return priceLowering;
    }

    public void setPriceLowering(int priceLowering) {
        this.priceLowering = priceLowering;
    }
    public String getNewPrice(){
        return "1900 din";
    }
    public void setImage(ArrayList<Integer> images) {
        this.images = images;
    }

    public int getCurrentImageIndex() {
        return currentImageIndex;
    }
    // 0 za klik u levo, 1 za klik u desno
    public void setCurrentImageIndex(int direction) {
        if(direction == 1){
            currentImageIndex++;
            if(currentImageIndex >= images.size())
                currentImageIndex = 0;
        }else{
            currentImageIndex--;
            if(currentImageIndex <= -1)
                currentImageIndex = images.size() - 1;
        }
    }

    public ArrayList<String> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(ArrayList<String> eventTypes) {
        this.eventTypes = eventTypes;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(category);
        dest.writeString(subCategory);
        dest.writeList(images);
        dest.writeList(eventTypes);
    }
    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
