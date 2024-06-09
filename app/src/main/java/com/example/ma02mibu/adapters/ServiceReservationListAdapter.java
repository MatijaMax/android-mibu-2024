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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;

import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.fragments.companyGrading.CompanyGradeFormFragment;

import com.example.ma02mibu.model.EmployeeReservation;
import com.example.ma02mibu.model.EventModel;
import com.example.ma02mibu.model.OurNotification;
import com.example.ma02mibu.model.ServiceReservationDTO;
import java.util.Locale;

public class ServiceReservationListAdapter extends ArrayAdapter<ServiceReservationDTO> {
    private List<ServiceReservationDTO> reservations;
    private FragmentActivity currFragActivity;

    public ServiceReservationListAdapter(Context context, List<ServiceReservationDTO> reservations, FragmentActivity fragmentActivity) {
        super(context, 0, reservations);
        this.reservations = reservations;
        currFragActivity = fragmentActivity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ServiceReservationDTO reservation = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.service_reservation_item_view, parent, false);
        }


        TextView employeeNameTextView = convertView.findViewById(R.id.employeeNameTextView);
        TextView eventOrganizerNameTextView = convertView.findViewById(R.id.eventOrganizerNameTextView);
        TextView serviceInfoTextView = convertView.findViewById(R.id.serviceInfoTextView);
        TextView cancelationDeadlineTextView = convertView.findViewById(R.id.cancelationDeadlineTextView);
        TextView statusTextView = convertView.findViewById(R.id.statusTextView);
        TextView packageTextView = convertView.findViewById(R.id.packageId);
        Button cancelButton = convertView.findViewById(R.id.cancelButton);
        handleCancelButtonClick(cancelButton, reservation, statusTextView);

        Button gradeButton = convertView.findViewById(R.id.buttonGrade);
        handleGradeButtonClick(gradeButton, reservation);

        if (reservation.getStatus() == EmployeeReservation.ReservationStatus.Finished || reservation.getStatus() == EmployeeReservation.ReservationStatus.CanceledByPUP) {
            gradeButton.setVisibility(View.VISIBLE);
        } else {
            gradeButton.setVisibility(View.GONE);
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
        eventOrganizerNameTextView.setText("Organizer: " + reservation.getEventOrganizerFirstName() + " " + reservation.getEventOrganizerLastName() + "\n" + reservation.getEventOrganizerEmail());
        serviceInfoTextView.setText("Service: " + reservation.getServiceName() + " - " + startDate + " to " + endDate);
        cancelationDeadlineTextView.setText("Cancelation deadline: " + reservation.getCancellationDeadline().getNumber() + " " + reservation.getCancellationDeadline().getDateFormat());
        statusTextView.setText("Status: " + reservation.getStatus().toString());
        packageTextView.setText("Package: " + reservation.getPackageRefId());


        return convertView;
    }

    private void handleGradeButtonClick(Button detailsButton, ServiceReservationDTO reservation){
        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(CompanyGradeFormFragment.newInstance(reservation.getEmployeeEmail()), currFragActivity,
                        true, R.id.scroll_services_res_list, "ServiceResPage");
            }
        });
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
                if(reservation.getStatus() == EmployeeReservation.ReservationStatus.Accepted){
                    Date startDate = reservation.getStart();
                    LocalDateTime localStartDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    Date endDate = reservation.getEnd();
                    LocalDateTime localEndDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                    EventModel eventModel = new EventModel(reservation.getServiceName(), localStartDate.format(dateFormatter).toString(), localStartDate.format(timeFormatter).toString(), localEndDate.format(timeFormatter).toString(), "reserved", reservation.getEmployeeEmail());
                    CloudStoreUtil.deleteEventModel(eventModel);
                }
                reservation.setStatus(EmployeeReservation.ReservationStatus.CanceledByOD);
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
                OurNotification notification = new OurNotification(reservation.getEmployeeEmail(), "Reservation canceled","Canceled reservation for " + reservation.getServiceName() + " by " + reservation.getEventOrganizerEmail(), "notRead");
                CloudStoreUtil.insertNotification(notification);
                statusTextView.setText("Status: " + reservation.getStatus().toString());
                detailsButton.setVisibility(View.GONE);
            }
        });
    }
}
