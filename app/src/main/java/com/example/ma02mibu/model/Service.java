package com.example.ma02mibu.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Service implements Parcelable {

    private Long id;
    private String name;
    private String description;
    private String category;
    private String subCategory;
    private int discount;
    private String specificity;
    private int priceByHour;
    private int minHourDuration;
    private int maxHourDuration;
    private int minMinutesDuration;
    private int maxMinutesDuration;
    private String location;
    private String reservationDeadline;
    private String cancellationDeadline;
    private boolean visible;
    private boolean availableToBuy;
    private boolean confirmAutomatically;
    private ArrayList<Integer> images;
    private ArrayList<String> eventTypes;
    private ArrayList<String> persons;
    private int currentImageIndex;

    public Service() {
        this.discount = 0;
    }

    public Service(Long id, String name, String description, String category, String subCategory, String specificity, int priceByHour, int minHourDuration, int minMinutesDuration, int maxHourDuration, int maxMinutesDuration, String location, String reservationDeadline,
                   String cancellationDeadline, ArrayList<Integer> images, ArrayList<String> eventTypes, ArrayList<String> persons, boolean confirmAutomatically) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.subCategory = subCategory;
        this.specificity = specificity;
        this.priceByHour = priceByHour;
        this.minHourDuration = minHourDuration;
        this.maxHourDuration = maxHourDuration;
        this.minMinutesDuration = minMinutesDuration;
        this.maxMinutesDuration = maxMinutesDuration;
        this.location = location;
        this.reservationDeadline = reservationDeadline;
        this.cancellationDeadline = cancellationDeadline;
        this.images = images;
        this.eventTypes = eventTypes;
        this.persons = persons;
        this.currentImageIndex = 0;
        this.discount = 0;
        this.confirmAutomatically = confirmAutomatically;
        this.visible = true;
        this.availableToBuy = true;
    }

    protected Service(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        category = in.readString();
        subCategory = in.readString();
        images = in.readArrayList(null);
        eventTypes = in.readArrayList(null);
        this.currentImageIndex = 0;
        this.discount = 0;
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

    public int getDiscount() {
        return discount;
    }
    public String getTotalPrice(){
        int minPrice = minHourDuration*priceByHour;
        if(minHourDuration != maxHourDuration){
            int maxPrice = maxHourDuration*priceByHour;
            minPrice = minPrice*(100-discount)/100;
            maxPrice = maxPrice*(100-discount)/100;
            return minPrice +"-"+maxPrice+" din";
        }
        else{
            minPrice = minPrice*(100-discount)/100;
            return minPrice+" din";
        }
    }
    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getSpecificity() {
        return specificity;
    }

    public void setSpecificity(String specificity) {
        this.specificity = specificity;
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

    public boolean isConfirmAutomatically() {
        return confirmAutomatically;
    }

    public void setConfirmAutomatically(boolean confirmAutomatically) {
        this.confirmAutomatically = confirmAutomatically;
    }

    public int getPriceByHour() {
        return priceByHour;
    }

    public void setPriceByHour(int priceByHour) {
        this.priceByHour = priceByHour;
    }

    public int getMinHourDuration() {
        return minHourDuration;
    }

    public void setMinHourDuration(int minHourDuration) {
        this.minHourDuration = minHourDuration;
    }

    public int getMaxHourDuration() {
        return maxHourDuration;
    }

    public void setMaxHourDuration(int maxHourDuration) {
        this.maxHourDuration = maxHourDuration;
    }

    public int getMinMinutesDuration() {
        return minMinutesDuration;
    }

    public void setMinMinutesDuration(int minMinutesDuration) {
        this.minMinutesDuration = minMinutesDuration;
    }

    public int getMaxMinutesDuration() {
        return maxMinutesDuration;
    }

    public void setMaxMinutesDuration(int maxMinutesDuration) {
        this.maxMinutesDuration = maxMinutesDuration;
    }

    public String getDuration(){
        if(minHourDuration == maxHourDuration && minMinutesDuration == maxMinutesDuration){
            return minHourDuration+" hours, "+ minMinutesDuration+" min";
        }
        else{
            return minHourDuration +":"+minMinutesDuration+" - "+ maxHourDuration+":"+maxMinutesDuration;
        }
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getReservationDeadline() {
        return reservationDeadline;
    }

    public void setReservationDeadline(String reservationDeadline) {
        this.reservationDeadline = reservationDeadline;
    }

    public String getCancellationDeadline() {
        return cancellationDeadline;
    }

    public void setCancellationDeadline(String cancellationDeadline) {
        this.cancellationDeadline = cancellationDeadline;
    }

    public ArrayList<Integer> getImages() {
        return images;
    }

    public void setImages(ArrayList<Integer> images) {
        this.images = images;
    }

    public ArrayList<String> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(ArrayList<String> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public ArrayList<String> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<String> persons) {
        this.persons = persons;
    }

    public int getCurrentImageIndex() {
        return currentImageIndex;
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
    public static final Creator<Service> CREATOR = new Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel in) {
            return new Service(in);
        }

        @Override
        public Service[] newArray(int size) {
            return new Service[size];
        }
    };

}
