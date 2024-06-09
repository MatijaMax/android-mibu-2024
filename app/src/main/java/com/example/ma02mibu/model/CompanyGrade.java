package com.example.ma02mibu.model;

import java.util.Date;

public class CompanyGrade {
    private int grade;
    private String comment;
    private String ownerRefId;
    private String organizersEmail;
    private Date createdDate;
    private boolean reported;
    private boolean deleted;
    private String uuid;

    public CompanyGrade() {
    }

    public CompanyGrade(int grade, String comment, String ownerRefId, Date createdDate, String organizersEmail, String uuid, boolean reported, boolean deleted) {
        this.grade = grade;
        this.comment = comment;
        this.ownerRefId = ownerRefId;
        this.createdDate = createdDate;
        this.organizersEmail = organizersEmail;
        this.uuid = uuid;
        this.reported = reported;
        this.deleted = deleted;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOrganizersEmail() {
        return organizersEmail;
    }

    public void setOrganizersEmail(String organizersEmail) {
        this.organizersEmail = organizersEmail;
    }

    public boolean isReported() {
        return reported;
    }

    public void setReported(boolean reported) {
        this.reported = reported;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getOwnerRefId() {
        return ownerRefId;
    }

    public void setOwnerRefId(String ownerRefId) {
        this.ownerRefId = ownerRefId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
