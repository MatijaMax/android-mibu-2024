package com.example.ma02mibu.model;

import java.util.Date;

public class ODReport {
    public enum REPORTSTATUS {REPORTED, ACCEPTED, DENIED}
    private String reason;
    private String organizerName;
    private String organizerId;
    private String reporterId;
    private String firestoreId;
    private Date dateOfReport;
    private ODReport.REPORTSTATUS status;
    private String reporterName;

    public ODReport() {
    }

    public ODReport(String reason, String organizerName, String organizerId, String reporterId, Date dateOfReport) {
        this.reason = reason;
        this.organizerName = organizerName;
        this.organizerId = organizerId;
        this.reporterId = reporterId;
        this.dateOfReport = dateOfReport;
        status = ODReport.REPORTSTATUS.REPORTED;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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


    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    public REPORTSTATUS getStatus() {
        return status;
    }

    public void setStatus(REPORTSTATUS status) {
        this.status = status;
    }
}
