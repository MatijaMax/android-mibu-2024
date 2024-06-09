package com.example.ma02mibu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.fragments.reporting.ProfilePageFragment;
import com.example.ma02mibu.model.CompanyReport;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.EmployeeReservation;
import com.example.ma02mibu.model.EventOrganizer;
import com.example.ma02mibu.model.ODReport;
import com.example.ma02mibu.model.OurNotification;
import com.example.ma02mibu.model.Owner;
import com.example.ma02mibu.model.ProfilePageData;
import com.example.ma02mibu.model.Service;
import com.example.ma02mibu.model.ServiceReservationDTO;

import java.util.ArrayList;

public class ODReportsAdapter extends ArrayAdapter<ODReport> {
    private ArrayList<ODReport> reports;
    private Context context;
    private FragmentActivity activity;
    public ODReportsAdapter(Context context, ArrayList<ODReport> odReports, FragmentActivity activity){
        super(context, R.layout.company_report_item, odReports);
        this.context = context;
        reports = odReports;
        this.activity = activity;
    }
    @Override
    public int getCount() {
        return reports.size();
    }

    @Nullable
    @Override
    public ODReport getItem(int position) {
        return reports.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ODReport report = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.company_report_item,
                    parent, false);
        }
        TextView companyName = convertView.findViewById(R.id.companyName);
        TextView reporterName = convertView.findViewById(R.id.reporterName);
        TextView reportDate = convertView.findViewById(R.id.reportDate);
        TextView reportReason = convertView.findViewById(R.id.reportReason);
        TextView reportStatus = convertView.findViewById(R.id.reportStatus);
        Button denyButton = convertView.findViewById(R.id.denyReport);
        Button accceptButton = convertView.findViewById(R.id.acceptReport);
        Button submitDenyBtn = convertView.findViewById(R.id.submitDenying);
        View finalConvertView = convertView;
        submitDenyBtn.setOnClickListener(v -> denyReport(report, finalConvertView));
        accceptButton.setOnClickListener(v -> acceptReport(report, finalConvertView));
        reporterName.setOnClickListener(v -> openOwnerProfile(report.getReporterId()));
        companyName.setOnClickListener(v -> openOrganizerProfile(report.getOrganizerId()));
        LinearLayout layout = convertView.findViewById(R.id.denyLayout);
        if(report.getStatus() != ODReport.REPORTSTATUS.REPORTED){
            denyButton.setVisibility(View.GONE);
            accceptButton.setVisibility(View.GONE);
        }
        denyButton.setOnClickListener(v -> {
            if(layout.getVisibility() == View.VISIBLE)
                layout.setVisibility(View.GONE);
            else
                layout.setVisibility(View.VISIBLE);
        });
        if(report != null){
            companyName.setText("OD name: "+report.getOrganizerName());
            reporterName.setText("User who reported: "+report.getReporterName());
            reportDate.setText("Report date: "+report.getDateOfReport());
            reportReason.setText("Report reason: "+report.getReason());
            reportStatus.setText("Report status: "+report.getStatus().toString());
        }
        return convertView;
    }

    private void denyReport(ODReport report, View convertView){
        report.setStatus(ODReport.REPORTSTATUS.DENIED);
        TextView reportStatus = convertView.findViewById(R.id.reportStatus);
        reportStatus.setText("Report status: "+report.getStatus().toString());
        Button denyButton = convertView.findViewById(R.id.denyReport);
        denyButton.setVisibility(View.GONE);
        Button acceptButton = convertView.findViewById(R.id.acceptReport);
        acceptButton.setVisibility(View.GONE);
        EditText reason = convertView.findViewById(R.id.denyReason);
        String reasonText = reason.getText().toString();
        LinearLayout layout = convertView.findViewById(R.id.denyLayout);
        layout.setVisibility(View.GONE);
        CloudStoreUtil.updateODReport(report);
        CloudStoreUtil.getOwner(report.getReporterId(), new CloudStoreUtil.OwnerCallback() {
            @Override
            public void onSuccess(Owner myItem) {
                OurNotification not = new OurNotification(myItem.getEmail(), "Report rejected", "Report for OD "+report.getOrganizerName()+" rejected, reason: "+reasonText,
                        "notRead");
                CloudStoreUtil.insertNotification(not);
                Toast.makeText(context, "Report denied", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(Exception e) {
            }
        });
    }

    private void acceptReport(ODReport report, View convertView){
        report.setStatus(ODReport.REPORTSTATUS.ACCEPTED);
        TextView reportStatus = convertView.findViewById(R.id.reportStatus);
        reportStatus.setText("Report status: "+report.getStatus().toString());
        Button denyButton = convertView.findViewById(R.id.denyReport);
        denyButton.setVisibility(View.GONE);
        Button acceptButton = convertView.findViewById(R.id.acceptReport);
        acceptButton.setVisibility(View.GONE);
        CloudStoreUtil.updateODReport(report);
        CloudStoreUtil.getEventOrganizer(report.getOrganizerId(), new CloudStoreUtil.EventOrganizerCallback() {
            @Override
            public void onSuccess(EventOrganizer myItem) {
                myItem.setBlocked(true);
                CloudStoreUtil.blockOrganizer(myItem);
                CloudStoreUtil.getServieReservationsList(new CloudStoreUtil.ServiceReservationsListCallback() {
                    @Override
                    public void onSuccess(ArrayList<EmployeeReservation> itemList) {
                        itemList.removeIf(r -> r.getStatus() != EmployeeReservation.ReservationStatus.New);
                        for(EmployeeReservation reservation : itemList){
                            if(reservation.getEventOrganizerEmail().equals(myItem.getEmail())){
                                reservation.setStatus(EmployeeReservation.ReservationStatus.CanceledByAdmin);
                                CloudStoreUtil.updateReservationStatus(reservation);
                                OurNotification not = new OurNotification(reservation.getEmployeeEmail(), "Reservation canceled",
                                        "Reservation for "+reservation.getEventOrganizerEmail()+ " canceled", "notRead");
                                CloudStoreUtil.insertNotification(not);
                            }
                        }
                    }
                    @Override
                    public void onFailure(Exception e) {
                        System.err.println("Error fetching documents: " + e.getMessage());
                    }
                });
                Toast.makeText(context, "Report accepted", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(Exception e) {
            }
        });
    }
    private void openOwnerProfile(String id){
        ProfilePageData profileData = new ProfilePageData();
        profileData.setRole(0);
        profileData.setUserId(id);
        FragmentTransition.to(ProfilePageFragment.newInstance(profileData), activity,
                true, R.id.scroll_od_reports_list, "profilePageOwner");
    }

    private void openOrganizerProfile(String id){
        ProfilePageData profileData = new ProfilePageData();
        profileData.setRole(1);
        profileData.setUserId(id);
        FragmentTransition.to(ProfilePageFragment.newInstance(profileData), activity,
                true, R.id.scroll_od_reports_list, "profilePageOrganizer");
    }
}
