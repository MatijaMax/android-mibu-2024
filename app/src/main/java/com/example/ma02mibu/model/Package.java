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
    private ArrayList<String> eventTypes;
    private String firestoreId;
    private String ownerUuid;
    public Package(){
        products = new ArrayList<>();
        services = new ArrayList<>();
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
        for (Service s: services) {
            images.addAll(s.getImages());
            eventTypesSet.addAll(s.getEventTypes());
        }
        for (Product p: products) {
            images.addAll(p.getImage());
            eventTypesSet.addAll(p.getEventTypes());
        }
        this.eventTypes.addAll(eventTypesSet);
        ownerUuid = "";
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
        int minPrice = 0;
        for(Product p: products)
            minPrice += p.getPrice();
        int maxPrice = minPrice;
        for(Service s: services) {
            minPrice += s.getMinPrice();
            maxPrice += s.getMaxPrice();
        }
        minPrice = minPrice * (100 - discount) / 100;
        maxPrice = maxPrice * (100 - discount) / 100;
        return minPrice+ " - " +maxPrice+ " din";
    }

    public int getMinPrice() {
        int minPrice = 0;
        for(Product p: products)
            minPrice += p.getPrice();
        for(Service s: services) {
            minPrice += s.getMinPrice();
        }
        return minPrice;
    }

    public int getMaxPrice() {
        int maxPrice = 0;
        for(Product p: products)
            maxPrice += p.getPrice();
        for(Service s: services) {
            maxPrice += s.getMaxPrice();
        }
        return maxPrice;
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

    public boolean containsProductName(String productName){
        for (Product p: products){
            if(p.getName().toLowerCase().contains(productName.toLowerCase()))
                return true;
        }
        return false;
    }

    public boolean containsServiceName(String serviceName){
        for (Service s: services){
            if(s.getName().toLowerCase().contains(serviceName.toLowerCase()))
                return true;
        }
        return false;
    }

    public ArrayList<Service> getServices() {
        return services;
    }

    public String getReservationConfirm() {
        if(services.isEmpty())
            return "/";
        String res = "Manually";
        for(Service s: services){
            if(s.isConfirmAutomatically()){
                res = "Automatically";
                break;
            }
        }
        return res;
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

    public String getOwnerUuid() {
        return ownerUuid;
    }

    public void setOwnerUuid(String ownerUuid) {
        this.ownerUuid = ownerUuid;
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

    public String getReservationDeadline(){
        String s="";
        Deadline deadline;
        if(!services.isEmpty()){
            deadline = services.get(0).getReservationDeadline();
            for (Service service: services){
                if(deadline.getDateFormat().equals("month")){
                    if(service.getReservationDeadline().getDateFormat().equals("days"))
                        deadline = service.getReservationDeadline();
                    else {
                        if (deadline.getNumber() > service.getReservationDeadline().getNumber())
                            deadline = service.getReservationDeadline();
                    }
                }else{
                    if(service.getReservationDeadline().getDateFormat().equals("days") && deadline.getNumber() > service.getReservationDeadline().getNumber())
                        deadline = service.getReservationDeadline();
                }
            }
            return deadline.getNumber() +" "+deadline.getDateFormat()+ " before start";
        }else{
            return "/";
        }
    }

    public String getCancellationDeadline(){
        String s="";
        Deadline deadline;
        if(!services.isEmpty()){
            deadline = services.get(0).getCancellationDeadline();
            for (Service service: services){
                if(deadline.getDateFormat().equals("month")){
                    if(service.getCancellationDeadline().getDateFormat().equals("days"))
                        deadline = service.getCancellationDeadline();
                    else {
                        if (deadline.getNumber() > service.getCancellationDeadline().getNumber())
                            deadline = service.getCancellationDeadline();
                    }
                }else{
                    if(service.getCancellationDeadline().getDateFormat().equals("days") && deadline.getNumber() > service.getCancellationDeadline().getNumber())
                        deadline = service.getCancellationDeadline();
                }
            }
            return deadline.getNumber() +" "+deadline.getDateFormat()+ " before start";
        }else{
            return "/";
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
