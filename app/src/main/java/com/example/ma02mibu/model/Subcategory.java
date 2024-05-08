package com.example.ma02mibu.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class Subcategory implements Parcelable {
    private String documentRefId;
    private String categoryId;
    private String name;
    private String description;
    private SUBCATEGORYTYPE type;
    public enum SUBCATEGORYTYPE { USLUGA, PROIZVOD }

    public Subcategory() { }

    public Subcategory(String categoryId, String name, String description, SUBCATEGORYTYPE type) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.type = type;
    }

    protected Subcategory(Parcel in){
        documentRefId = in.readString();
        name = in.readString();
        description = in.readString();
        categoryId = in.readString();
        type = SUBCATEGORYTYPE.values()[in.readInt()];
    }

    @Exclude
    public String getDocumentRefId() {
        return documentRefId;
    }

    public void setDocumentRefId(String documentRefId) {
        this.documentRefId = documentRefId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
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
        dest.writeString(documentRefId);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(categoryId);
        dest.writeInt(type.ordinal());
    }

    public static final Creator<Subcategory> CREATOR = new Creator<Subcategory>() {
        @Override
        public Subcategory createFromParcel(Parcel in) {
            return new Subcategory(in);
        }

        @Override
        public Subcategory[] newArray(int size) {
            return new Subcategory[size];
        }
    };
}