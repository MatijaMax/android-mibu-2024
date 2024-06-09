package com.example.ma02mibu.model;

import java.util.Date;

public class CompanyGradeReport {
    private String CompanyGradeUID;
    private int grade;
    private String comment;
    private String reason;
    private String ownersEmail;
    private Date reportedDate;
    private REPORTSTATUS reportStatus;
    public enum REPORTSTATUS { REPORTED, ACCEPTED, REJECTED }

    public CompanyGradeReport() {
    }

    public CompanyGradeReport(String companyGradeUID, String reason, Date reportedDate, REPORTSTATUS reportStatus, int grade, String comment, String ownersEmail) {
        CompanyGradeUID = companyGradeUID;
        this.reason = reason;
        this.reportedDate = reportedDate;
        this.reportStatus = reportStatus;
        this.grade = grade;
        this.comment = comment;
        this.ownersEmail = ownersEmail;
    }

    public String getOwnersEmail() {
        return ownersEmail;
    }

    public void setOwnersEmail(String ownersEmail) {
        this.ownersEmail = ownersEmail;
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

    public String getCompanyGradeUID() {
        return CompanyGradeUID;
    }

    public void setCompanyGradeUID(String companyGradeUID) {
        CompanyGradeUID = companyGradeUID;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(Date reportedDate) {
        this.reportedDate = reportedDate;
    }

    public REPORTSTATUS getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(REPORTSTATUS reportStatus) {
        this.reportStatus = reportStatus;
    }
}
