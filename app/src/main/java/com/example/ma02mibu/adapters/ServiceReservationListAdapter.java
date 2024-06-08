package com.example.ma02mibu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import com.example.ma02mibu.R;

import com.example.ma02mibu.model.EmployeeReservation;
import com.example.ma02mibu.model.ServiceReservationDTO;

import java.util.List;
import java.util.Locale;

public class ServiceReservationListAdapter extends ArrayAdapter<ServiceReservationDTO> {
    private List<ServiceReservationDTO> reservations;

    public ServiceReservationListAdapter(Context context, List<ServiceReservationDTO> reservations) {
        super(context, 0, reservations);
        this.reservations = reservations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ServiceReservationDTO reservation = getItem(position);


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.service_reservation_item_view, parent, false);
        }


        TextView employeeNameTextView = convertView.findViewById(R.id.employeeNameTextView);
        TextView eventOrganizerNameTextView = convertView.findViewById(R.id.eventOrganizerNameTextView);
        TextView serviceInfoTextView = convertView.findViewById(R.id.serviceInfoTextView);
        TextView cancelationDeadlineTextView = convertView.findViewById(R.id.cancelationDeadlineTextView);
        TextView statusTextView = convertView.findViewById(R.id.statusTextView);
        Button cancelButton = convertView.findViewById(R.id.cancelButton);

        // Format dates
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String startDate = dateFormat.format(reservation.getStart());
        String endDate = dateFormat.format(reservation.getEnd());

        employeeNameTextView.setText("Employee: " + reservation.getEmployeeFirstName() + " " + reservation.getEmployeeLastName());
        eventOrganizerNameTextView.setText("Organizer: " + reservation.getEventOrganizerFirstName() + " " + reservation.getEventOrganizerLastName());
        serviceInfoTextView.setText("Service: " + reservation.getServiceName() + " - " + startDate + " to " + endDate);
        cancelationDeadlineTextView.setText("Cancelation deadline: " + reservation.getCancellationDeadline().getNumber() + " " + reservation.getCancellationDeadline().getDateFormat());
        statusTextView.setText("Status: " + reservation.getStatus().toString());

        if (reservation.getStatus() == EmployeeReservation.ReservationStatus.New || reservation.getStatus() == EmployeeReservation.ReservationStatus.Accepted) {
            cancelButton.setVisibility(View.VISIBLE);
        } else {
            cancelButton.setVisibility(View.GONE);
        }

        return convertView;
    }
}
