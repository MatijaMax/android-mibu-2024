package com.example.ma02mibu.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Product implements Parcelable {
    private Long id;
    private String firestoreId;
    private String name;
    private String description;
    private String category;
    private String subCategory;
    private String ownerUuid;
    private int price;
    private int discount;
    private boolean visible;
    private boolean availableToBuy;
    private ArrayList<Integer> images;
    private ArrayList<String> eventTypes;
    private int currentImageIndex;
    private boolean pending;
    public Product(Long id, String name, String description, String category, String subCategory, int price, ArrayList<Integer> images, ArrayList<String> eventTypes, int discount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.subCategory = subCategory;
        this.price = price;
        this.images = images;
        this.eventTypes = eventTypes;
        this.currentImageIndex = 0;
        this.discount = discount;
        this.visible = true;
        this.availableToBuy = true;
        pending = false;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isAvailableToBuy() {
        return availableToBuy;
    }

    public void setAvailableToBuy(boolean availableToBuy) {
        this.availableToBuy = availableToBuy;
    }

    public ArrayList<Integer> getImage() {
        return images;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
    public String getNewPrice(){
        if(discount == 0){
            return price + " din";
        }
        else{
            double newPrice = ((100-discount)/100.0)*price;
            return newPrice+" din";
        }
    }

    public int getNewPriceValue(){
        if(discount == 0){
            return price;
        }
        else{
            double newPrice = ((100-discount)/100.0)*price;
            return (int)newPrice;
        }
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

    public boolean containsEventType(String eventType){
        for(String productEventType: eventTypes){
            if(productEventType.toLowerCase().contains(eventType))
                return true;
        }
        return false;
    }

    public String getOwnerUuid() {
        return ownerUuid;
    }

    public void setOwnerUuid(String ownerUuid) {
        this.ownerUuid = ownerUuid;
    }

    public String getFirestoreId() {
        return firestoreId;
    }

    public void setFirestoreId(String firestoreId) {
        this.firestoreId = firestoreId;
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

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
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
