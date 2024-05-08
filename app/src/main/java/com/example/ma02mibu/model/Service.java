package com.example.ma02mibu.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Service implements Parcelable {

    private Long id;
    private String firestoreId;
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
    private String ownerUuid;
    private Deadline reservationDeadline;
    private Deadline cancellationDeadline;
    private boolean visible;
    private boolean availableToBuy;
    private boolean confirmAutomatically;
    private ArrayList<Integer> images;
    private ArrayList<String> eventTypes;
    private ArrayList<EmployeeInService> persons;
    private int currentImageIndex;

    public Service() {
        this.discount = 0;
    }

    public Service(Long id, String name, String description, String category, String subCategory, String specificity, int priceByHour, int minHourDuration, int minMinutesDuration, int maxHourDuration, int maxMinutesDuration, String location, Deadline reservationDeadline,
                   Deadline cancellationDeadline, ArrayList<Integer> images, ArrayList<String> eventTypes, ArrayList<EmployeeInService> persons, boolean confirmAutomatically) {
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
        ownerUuid = "";
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

    public int getMinPrice(){
        int minPrice = minHourDuration*priceByHour;
        return minPrice;
    }
    public int getMaxPrice(){
        int maxPrice = maxHourDuration*priceByHour;
        return maxPrice;
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

    public Deadline getReservationDeadline() {
        return reservationDeadline;
    }

    public String getReservationDeadlineText() {
        return reservationDeadline.getNumber() + reservationDeadline.getDateFormat() + " before start";
    }

    public String getCancellationDeadlineText() {
        return cancellationDeadline.getNumber() + cancellationDeadline.getDateFormat() + " before start";
    }

    public void setReservationDeadline(Deadline reservationDeadline) {
        this.reservationDeadline = reservationDeadline;
    }

    public Deadline getCancellationDeadline() {
        return cancellationDeadline;
    }

    public void setCancellationDeadline(Deadline cancellationDeadline) {
        this.cancellationDeadline = cancellationDeadline;
    }

    public String getOwnerUuid() {
        return ownerUuid;
    }

    public void setOwnerUuid(String ownerUuid) {
        this.ownerUuid = ownerUuid;
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

    public ArrayList<EmployeeInService> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<EmployeeInService> persons) {
        this.persons = persons;
    }

    public boolean containsPerson(String employee){
        for(EmployeeInService person: persons){
            if(person.getFirstName().toLowerCase().contains(employee))
                return true;
        }
        return false;
    }

    public boolean containsEventType(String eventType){
        for(String productEventType: eventTypes){
            if(productEventType.toLowerCase().contains(eventType))
                return true;
        }
        return false;
    }

    public int getCurrentImageIndex() {
        return currentImageIndex;
    }

    public void setCurrentImageIndex(int direction) {
        /*if(direction == 1){
            currentImageIndex++;
            if(currentImageIndex >= images.size())
                currentImageIndex = 0;
        }else{
            currentImageIndex--;
            if(currentImageIndex <= -1)
                currentImageIndex = images.size() - 1;
        }*/
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
