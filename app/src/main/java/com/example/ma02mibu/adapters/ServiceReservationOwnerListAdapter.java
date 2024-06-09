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

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.fragments.packages.PackageListFragment;
import com.example.ma02mibu.fragments.reporting.OrganizerProfilePageFragment;
import com.example.ma02mibu.model.EmployeeReservation;
import com.example.ma02mibu.model.OrganizerProfilePageData;
import com.example.ma02mibu.model.ServiceReservationDTO;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ServiceReservationOwnerListAdapter extends ArrayAdapter<ServiceReservationDTO> {
    private List<ServiceReservationDTO> reservations;
    private FragmentActivity currFragActivity;

    public ServiceReservationOwnerListAdapter(Context context, List<ServiceReservationDTO> reservations, FragmentActivity fragmentActivity) {
        super(context, 0, reservations);
        this.reservations = reservations;
        currFragActivity = fragmentActivity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ServiceReservationDTO reservation = getItem(position);


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.service_reservation_item_owner_view, parent, false);
        }


        TextView employeeNameTextView = convertView.findViewById(R.id.employeeNameTextView);
        TextView eventOrganizerNameTextView = convertView.findViewById(R.id.eventOrganizerNameTextView);
        TextView serviceInfoTextView = convertView.findViewById(R.id.serviceInfoTextView);
        TextView cancelationDeadlineTextView = convertView.findViewById(R.id.cancelationDeadlineTextView);
        TextView statusTextView = convertView.findViewById(R.id.statusTextView);
        eventOrganizerNameTextView.setOnClickListener(v -> openOrganizerProfile(reservation));
        Button cancelButton = convertView.findViewById(R.id.cancelButton);
        handleCancelButtonClick(cancelButton, reservation, statusTextView);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String startDate = dateFormat.format(reservation.getStart());
        String endDate = dateFormat.format(reservation.getEnd());

        employeeNameTextView.setText("Employee: " + reservation.getEmployeeFirstName() + " " + reservation.getEmployeeLastName() + "\n" + reservation.getEmployeeEmail());
        eventOrganizerNameTextView.setText("Organizer: " + reservation.getEventOrganizerFirstName() + " " + reservation.getEventOrganizerLastName() + "\n" + reservation.getEventOrganizerEmail());
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

    private void handleCancelButtonClick(Button detailsButton, ServiceReservationDTO reservation, TextView statusTextView){
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
                    }
                    @Override
                    public void onFailure(Exception e) {
                        System.err.println("Error updating item: " + e.getMessage());
                    }
                });
                statusTextView.setText("Status: " + reservation.getStatus().toString());
                detailsButton.setVisibility(View.GONE);
            }
        });
    }
    private void openOrganizerProfile(ServiceReservationDTO reservationDTO){
        OrganizerProfilePageData profilePageData = new OrganizerProfilePageData();
        profilePageData.setEmail(reservationDTO.getEventOrganizerEmail());
        FragmentTransition.to(OrganizerProfilePageFragment.newInstance(profilePageData), currFragActivity,
                true, R.id.scroll_services_res_list_owner, "eventOrganizerProfile");
    }

}
