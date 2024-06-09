package com.example.ma02mibu.model;

import java.util.Date;

public class CompanyReport {
    public enum REPORTSTATUS {REPORTED, ACCEPTED, DENIED}
    private String reason;
    private String companyName;
    private String ownerUuid;
    private String reporterId;
    private String firestoreId;
    private Date dateOfReport;
    private REPORTSTATUS status;
    private String reporterName;

    public CompanyReport() {
    }

    public CompanyReport(String reason, String companyName, String ownerUuid, String reporterId, Date dateOfReport) {
        this.reason = reason;
        this.companyName = companyName;
        this.ownerUuid = ownerUuid;
        this.reporterId = reporterId;
        this.dateOfReport = dateOfReport;
        status = REPORTSTATUS.REPORTED;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOwnerUuid() {
        return ownerUuid;
    }

    public void setOwnerUuid(String ownerUuid) {
        this.ownerUuid = ownerUuid;
    }

    public String getReporterId() {
        return reporterId;
    }

    public void setReporterId(String reporterId) {
        this.reporterId = reporterId;
    }

    public String getFirestoreId() {
        return firestoreId;
    }

    public void setFirestoreId(String firestoreId) {
        this.firestoreId = firestoreId;
    }

    public Date getDateOfReport() {
        return dateOfReport;
    }

    public void setDateOfReport(Date dateOfReport) {
        this.dateOfReport = dateOfReport;
    }

    public REPORTSTATUS getStatus() {
        return status;
    }

    public void setStatus(REPORTSTATUS status) {
        this.status = status;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }
}
