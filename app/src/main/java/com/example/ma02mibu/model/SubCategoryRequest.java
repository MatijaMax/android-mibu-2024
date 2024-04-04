package com.example.ma02mibu.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class SubCategoryRequest implements Parcelable {
    private Long id;
    private Long categoryId;
    private String name;
    private String description;
    private SubCategory.SUBCATEGORYTYPE type;
    private Long userId;

    public SubCategoryRequest() { }

    public SubCategoryRequest(Long id, Long categoryId, String name, String description, SubCategory.SUBCATEGORYTYPE type, Long userId) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.type = type;
        this.userId = userId;
    }

    protected SubCategoryRequest(Parcel in){
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        categoryId = in.readLong();
        type = SubCategory.SUBCATEGORYTYPE.values()[in.readInt()];
        userId = in.readLong();
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

    public SubCategory.SUBCATEGORYTYPE getType() {
        return type;
    }

    public void setType(SubCategory.SUBCATEGORYTYPE type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
        dest.writeLong(userId);
    }

    public static final Creator<SubCategoryRequest> CREATOR = new Creator<SubCategoryRequest>() {
        @Override
        public SubCategoryRequest createFromParcel(Parcel in) {
            return new SubCategoryRequest(in);
        }

        @Override
        public SubCategoryRequest[] newArray(int size) {
            return new SubCategoryRequest[size];
        }
    };
}
