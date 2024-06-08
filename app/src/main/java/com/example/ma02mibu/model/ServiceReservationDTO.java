package com.example.ma02mibu.model;

import java.util.Date;

public class ServiceReservationDTO {
    private String employeeEmail;
    private String employeeFirstName;
    private String employeeLastName;
    private String eventOrganizerEmail;
    private String eventOrganizerFirstName;
    private String eventOrganizerLastName;
    private String serviceRefId;
    private String serviceName;
    private boolean confirmAutomatically;
    private Deadline cancellationDeadline;
    private Date start;
    private Date end;
    private String packageRefId;
    private EmployeeReservation.ReservationStatus status;

    public ServiceReservationDTO() {

    }

    public ServiceReservationDTO(String employeeEmail, String employeeFirstName, String employeeLastName, String eventOrganizerEmail, String eventOrganizerFirstName, String eventOrganizerLastName, String serviceRefId, String serviceName, boolean confirmAutomatically, Deadline cancellationDeadline, Date start, Date end, String packageRefId, EmployeeReservation.ReservationStatus status) {
        this.employeeEmail = employeeEmail;
        this.employeeFirstName = employeeFirstName;
        this.employeeLastName = employeeLastName;
        this.eventOrganizerEmail = eventOrganizerEmail;
        this.eventOrganizerFirstName = eventOrganizerFirstName;
        this.eventOrganizerLastName = eventOrganizerLastName;
        this.serviceRefId = serviceRefId;
        this.serviceName = serviceName;
        this.confirmAutomatically = confirmAutomatically;
        this.cancellationDeadline = cancellationDeadline;
        this.start = start;
        this.end = end;
        this.packageRefId = packageRefId;
        this.status = status;
    }

    public ServiceReservationDTO(EmployeeReservation employeeReservation) {
        this.employeeEmail = employeeReservation.getEmployeeEmail();
        this.eventOrganizerEmail = employeeReservation.getEventOrganizerEmail();
        this.serviceRefId = employeeReservation.getServiceRefId();
        this.start = employeeReservation.getStart();
        this.end = employeeReservation.getEnd();
        this.packageRefId = employeeReservation.getPackageRefId();
        this.status = employeeReservation.getStatus();
    }
    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String getEmployeeFirstName() {
        return employeeFirstName;
    }

    public void setEmployeeFirstName(String employeeFirstName) {
        this.employeeFirstName = employeeFirstName;
    }

    public String getEmployeeLastName() {
        return employeeLastName;
    }

    public void setEmployeeLastName(String employeeLastName) {
        this.employeeLastName = employeeLastName;
    }

    public String getEventOrganizerEmail() {
        return eventOrganizerEmail;
    }

    public void setEventOrganizerEmail(String eventOrganizerEmail) {
        this.eventOrganizerEmail = eventOrganizerEmail;
    }

    public String getEventOrganizerFirstName() {
        return eventOrganizerFirstName;
    }

    public void setEventOrganizerFirstName(String eventOrganizerFirstName) {
        this.eventOrganizerFirstName = eventOrganizerFirstName;
    }

    public String getEventOrganizerLastName() {
        return eventOrganizerLastName;
    }

    public void setEventOrganizerLastName(String eventOrganizerLastName) {
        this.eventOrganizerLastName = eventOrganizerLastName;
    }

    public String getServiceRefId() {
        return serviceRefId;
    }

    public void setServiceRefId(String serviceRefId) {
        this.serviceRefId = serviceRefId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public boolean isConfirmAutomatically() {
        return confirmAutomatically;
    }

    public void setConfirmAutomatically(boolean confirmAutomatically) {
        this.confirmAutomatically = confirmAutomatically;
    }

    public Deadline getCancellationDeadline() {
        return cancellationDeadline;
    }

    public void setCancellationDeadline(Deadline cancellationDeadline) {
        this.cancellationDeadline = cancellationDeadline;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getPackageRefId() {
        return packageRefId;
    }

    public void setPackageRefId(String packageRefId) {
        this.packageRefId = packageRefId;
    }

    public EmployeeReservation.ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(EmployeeReservation.ReservationStatus status) {
        this.status = status;
    }
}
