package com.example.ma02mibu.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class SubcategoryProposal implements Parcelable {
    private Subcategory subcategory;
    //ID servisa ili usluge
    private String itemId;
    public SubcategoryProposal(){}

    public SubcategoryProposal(Subcategory subcategory, String itemId) {
        this.subcategory = subcategory;
        this.itemId = itemId;
    }

    protected SubcategoryProposal(Parcel in) {
        subcategory = in.readParcelable(Subcategory.class.getClassLoader());
        itemId = in.readString();
    }

    public static final Creator<SubcategoryProposal> CREATOR = new Creator<SubcategoryProposal>() {
        @Override
        public SubcategoryProposal createFromParcel(Parcel in) {
            return new SubcategoryProposal(in);
        }

        @Override
        public SubcategoryProposal[] newArray(int size) {
            return new SubcategoryProposal[size];
        }
    };

    public Subcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeParcelable(subcategory, flags);
        dest.writeString(itemId);
    }
}
