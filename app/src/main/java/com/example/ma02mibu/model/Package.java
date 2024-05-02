package com.example.ma02mibu.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Package implements Parcelable {
    private Long id;
    private String name;
    private String description;
    private String category;
    private int discount;
    private boolean availableToBuy;
    private boolean visible;
    private ArrayList<Integer> images;
    private ArrayList<Service> services;
    private ArrayList<Product> products;
    private int currentImageIndex;
    private String price;
    private ArrayList<String> eventTypes;
    private String firestoreId;
    public Package(){

    }

    public Package(Long id, String name, String description, String category, int discount, ArrayList<Service> services, ArrayList<Product> products) {
        currentImageIndex = 0;
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.discount = discount;
        this.services = services;
        this.products = products;
        images = new ArrayList<>();
        eventTypes = new ArrayList<>();
        Set<String> eventTypesSet = new HashSet<>();
        price = "18000";
        for (Service s: services) {
            images.addAll(s.getImages());
            eventTypesSet.addAll(s.getEventTypes());
        }
        for (Product p: products) {
            images.addAll(p.getImage());
            eventTypesSet.addAll(p.getEventTypes());
        }
        this.eventTypes.addAll(eventTypesSet);
    }

    protected Package(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        category = in.readString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ArrayList<String> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(ArrayList<String> eventTypes) {
        this.eventTypes = eventTypes;
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

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public ArrayList<Service> getServices() {
        return services;
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
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
    public ArrayList<Integer> getImages() {
        return images;
    }
    public int getCurrentImageIndex() {
        return currentImageIndex;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<Package> CREATOR = new Creator<Package>() {
        @Override
        public Package createFromParcel(Parcel in) {
            return new Package(in);
        }

        @Override
        public Package[] newArray(int size) {
            return new Package[size];
        }
    };
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(category);
    }
}
