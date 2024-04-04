package com.example.ma02mibu.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class SubCategory implements Parcelable {
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

    protected SubCategory(Parcel in){
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        categoryId = in.readLong();
        type = SUBCATEGORYTYPE.values()[in.readInt()];
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeLong(categoryId);
        dest.writeInt(type.ordinal());
    }

    public static final Creator<SubCategory> CREATOR = new Creator<SubCategory>() {
        @Override
        public SubCategory createFromParcel(Parcel in) {
            return new SubCategory(in);
        }

        @Override
        public SubCategory[] newArray(int size) {
            return new SubCategory[size];
        }
    };
}