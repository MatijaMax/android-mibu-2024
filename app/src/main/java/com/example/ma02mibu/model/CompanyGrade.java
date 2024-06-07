package com.example.ma02mibu.model;

import java.util.Date;

public class CompanyGrade {
    private int grade;
    private String comment;
    private String ownerRefId;
    private String organizersEmail;
    private Date createdDate;

    public CompanyGrade() {
    }

    public CompanyGrade(int grade, String comment, String ownerRefId, Date createdDate, String organizersEmail) {
        this.grade = grade;
        this.comment = comment;
        this.ownerRefId = ownerRefId;
        this.createdDate = createdDate;
        this.organizersEmail = organizersEmail;
    }

    public String getOrganizersEmail() {
        return organizersEmail;
    }

    public void setOrganizersEmail(String organizersEmail) {
        this.organizersEmail = organizersEmail;
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
