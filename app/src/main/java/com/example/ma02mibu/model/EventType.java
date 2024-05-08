package com.example.ma02mibu.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;

public class EventType implements Parcelable {
    private String documentRefId;
    private String name;
    private String description;
    private EVENTTYPESTATUS status;
    private ArrayList<String> suggestedSubcategoryDocRefId;

    protected EventType(Parcel in) {
        documentRefId = in.readString();
        name = in.readString();
        description = in.readString();
        suggestedSubcategoryDocRefId = in.createStringArrayList();
        status = EVENTTYPESTATUS.values()[in.readInt()];
    }

    public static final Creator<EventType> CREATOR = new Creator<EventType>() {
        @Override
        public EventType createFromParcel(Parcel in) {
            return new EventType(in);
        }

        @Override
        public EventType[] newArray(int size) {
            return new EventType[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(documentRefId);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeStringList(suggestedSubcategoryDocRefId);
        dest.writeInt(status.ordinal());
    }

    public enum EVENTTYPESTATUS  {ACTIVE, DEACTIVATED};

    public EventType() {
    }

    public EventType(String documentRefId, String name, String description, EVENTTYPESTATUS status) {
        this.documentRefId = documentRefId;
        this.name = name;
        this.description = description;
        this.suggestedSubcategoryDocRefId = new ArrayList<>();
        this.status = status;
    }

    @Exclude
    public String getDocumentRefId() {
        return documentRefId;
    }

    public void setDocumentRefId(String documentRefId) {
        this.documentRefId = documentRefId;
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

    public EVENTTYPESTATUS getStatus() {
        return status;
    }

    public void setStatus(EVENTTYPESTATUS status) {
        this.status = status;
    }

    public ArrayList<String> getSuggestedSubcategoryDocRefId() {
        return suggestedSubcategoryDocRefId;
    }

    public void setSuggestedSubcategoryDocRefId(ArrayList<String> suggestedSubcategoryDocRefId) {
        this.suggestedSubcategoryDocRefId = suggestedSubcategoryDocRefId;
    }
}
