package com.example.ma02mibu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.model.EmployeeReservation;
import com.example.ma02mibu.model.ServiceReservationDTO;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ServiceReservationEmployeeListAdapter extends ArrayAdapter<ServiceReservationDTO> {
    private List<ServiceReservationDTO> reservations;
    private FragmentActivity currFragActivity;

    public ServiceReservationEmployeeListAdapter(Context context, List<ServiceReservationDTO> reservations, FragmentActivity fragmentActivity) {
        super(context, 0, reservations);
        this.reservations = reservations;
        currFragActivity = fragmentActivity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ServiceReservationDTO reservation = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.service_reservation_item_employee_view, parent, false);
        }


        TextView employeeNameTextView = convertView.findViewById(R.id.employeeNameTextView);
        TextView eventOrganizerNameTextView = convertView.findViewById(R.id.eventOrganizerNameTextView);
        TextView serviceInfoTextView = convertView.findViewById(R.id.serviceInfoTextView);
        TextView cancelationDeadlineTextView = convertView.findViewById(R.id.cancelationDeadlineTextView);
        TextView statusTextView = convertView.findViewById(R.id.statusTextView);
        TextView packageTextView = convertView.findViewById(R.id.packageId);
        Button cancelButton = convertView.findViewById(R.id.cancelButton);
        Button acceptButton = convertView.findViewById(R.id.acceptButton);
        handleCancelButtonClick(cancelButton, reservation, statusTextView, acceptButton);
        handleAcceptButtonClick(acceptButton, reservation, statusTextView, cancelButton);

        if (reservation.getStatus() == EmployeeReservation.ReservationStatus.New) {
            acceptButton.setVisibility(View.VISIBLE);
        } else {
            acceptButton.setVisibility(View.GONE);
        }

        if (reservation.getStatus() == EmployeeReservation.ReservationStatus.New || reservation.getStatus() == EmployeeReservation.ReservationStatus.Accepted) {
            cancelButton.setVisibility(View.VISIBLE);
        } else {
            cancelButton.setVisibility(View.GONE);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String startDate = dateFormat.format(reservation.getStart());
        String endDate = dateFormat.format(reservation.getEnd());

        employeeNameTextView.setText("Employee: " + reservation.getEmployeeFirstName() + " " + reservation.getEmployeeLastName() + "\n" + reservation.getEmployeeEmail());
        eventOrganizerNameTextView.setText("Organizer: " + reservation.getEventOrganizerFirstName() + " " + reservation.getEventOrganizerLastName()  + "\n" + reservation.getEventOrganizerEmail());
        serviceInfoTextView.setText("Service: " + reservation.getServiceName() + " - " + startDate + " to " + endDate);
        cancelationDeadlineTextView.setText("Cancelation deadline: " + reservation.getCancellationDeadline().getNumber() + " " + reservation.getCancellationDeadline().getDateFormat());
        statusTextView.setText("Status: " + reservation.getStatus().toString());
        packageTextView.setText("Package: " + reservation.getPackageRefId());


        return convertView;
    }

    private void handleAcceptButtonClick(Button detailsButton, ServiceReservationDTO reservation, TextView statusTextView, Button cancelButton){
        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservation.setStatus(EmployeeReservation.ReservationStatus.Accepted);
                CloudStoreUtil.updateStatusReservation(reservation, new CloudStoreUtil.UpdateReadCallback() {
                    @Override
                    public void onSuccess() {

                        Toast.makeText(v.getContext() , "Accepted", Toast.LENGTH_SHORT).show();
                        System.out.println("Item updated!");
                    }
                    @Override
                    public void onFailure(Exception e) {
                        System.err.println("Error updating item: " + e.getMessage());
                    }
                });
                statusTextView.setText("Status: " + reservation.getStatus().toString());
                detailsButton.setVisibility(View.GONE);
                cancelButton.setVisibility(View.GONE);
            }
        });
    }

    private void handleCancelButtonClick(Button detailsButton, ServiceReservationDTO reservation, TextView statusTextView, Button acceptButton){
        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentDate = new Date();
                LocalDate localDate = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate localDateStart = reservation.getStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate cancelLocalDate;
                if(reservation.getCancellationDeadline().getDateFormat().equals("days")){
                    cancelLocalDate = localDate.plusDays(reservation.getCancellationDeadline().getNumber());
                }else{
                    cancelLocalDate = localDate.plusMonths(reservation.getCancellationDeadline().getNumber());
                }
                if(cancelLocalDate.isAfter(localDateStart)){
                    Toast.makeText(v.getContext() , "Cancellation deadline passed!", Toast.LENGTH_SHORT).show();
                    return;
                }
                reservation.setStatus(EmployeeReservation.ReservationStatus.CanceledByPUP);
                CloudStoreUtil.updateStatusReservation(reservation, new CloudStoreUtil.UpdateReadCallback() {
                    @Override
                    public void onSuccess() {

                        Toast.makeText(v.getContext() , "Canceled", Toast.LENGTH_SHORT).show();
                        System.out.println("Item updated!");
                    }
                    @Override
                    public void onFailure(Exception e) {
                        System.err.println("Error updating item: " + e.getMessage());
                    }
                });
                statusTextView.setText("Status: " + reservation.getStatus().toString());
                detailsButton.setVisibility(View.GONE);
                acceptButton.setVisibility(View.GONE);
            }
        });
    }
}
