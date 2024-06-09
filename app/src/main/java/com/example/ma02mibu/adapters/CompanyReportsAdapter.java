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
import com.example.ma02mibu.fragments.pricelist.EditServicePriceFragment;
import com.example.ma02mibu.fragments.reporting.CompanyReportsListFragment;
import com.example.ma02mibu.fragments.reporting.ProfilePageFragment;
import com.example.ma02mibu.model.CompanyReport;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.EmployeeReservation;
import com.example.ma02mibu.model.EventOrganizer;
import com.example.ma02mibu.model.OurNotification;
import com.example.ma02mibu.model.Owner;
import com.example.ma02mibu.model.Package;
import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.model.ProfilePageData;
import com.example.ma02mibu.model.Service;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CompanyReportsAdapter extends ArrayAdapter<CompanyReport> {
    private ArrayList<CompanyReport> reports;
    private Context context;
    private FragmentActivity activity;
    public CompanyReportsAdapter(Context context, ArrayList<CompanyReport> companyReports, FragmentActivity activity){
        super(context, R.layout.company_report_item, companyReports);
        this.context = context;
        reports = companyReports;
        this.activity = activity;
    }
    @Override
    public int getCount() {
        return reports.size();
    }

    @Nullable
    @Override
    public CompanyReport getItem(int position) {
        return reports.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CompanyReport report = getItem(position);
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
        accceptButton.setOnClickListener(v -> acceptReport(report, finalConvertView));
        submitDenyBtn.setOnClickListener(v -> denyReport(report, finalConvertView));
        companyName.setOnClickListener(v -> openOwnerProfile(report.getOwnerUuid()));
        reporterName.setOnClickListener(v -> openOrganizerProfile(report.getReporterId()));
        LinearLayout layout = convertView.findViewById(R.id.denyLayout);
        if(report.getStatus() != CompanyReport.REPORTSTATUS.REPORTED){
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
            companyName.setText("Company name: "+report.getCompanyName());
            reporterName.setText("User who reported: "+report.getReporterName());
            reportDate.setText("Report date: "+report.getDateOfReport());
            reportReason.setText("Report reason: "+report.getReason());
            reportStatus.setText("Report status: "+report.getStatus().toString());
        }
        return convertView;
    }
    private void denyReport(CompanyReport report, View convertView){
        report.setStatus(CompanyReport.REPORTSTATUS.DENIED);
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
        CloudStoreUtil.updateCompanyReport(report);
        CloudStoreUtil.getEventOrganizer(report.getReporterId(), new CloudStoreUtil.EventOrganizerCallback() {
            @Override
            public void onSuccess(EventOrganizer myItem) {
                OurNotification not = new OurNotification(myItem.getEmail(), "Report rejected", "Report on company "+report.getCompanyName()+" rejected, reason: "+reasonText,
                        "notRead");
                CloudStoreUtil.insertNotification(not);
                Toast.makeText(context, "Report denied", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(Exception e) {
            }
        });
    }

    private void acceptReport(CompanyReport report, View convertView){
        report.setStatus(CompanyReport.REPORTSTATUS.ACCEPTED);
        TextView reportStatus = convertView.findViewById(R.id.reportStatus);
        reportStatus.setText("Report status: "+report.getStatus().toString());
        Button denyButton = convertView.findViewById(R.id.denyReport);
        denyButton.setVisibility(View.GONE);
        Button acceptButton = convertView.findViewById(R.id.acceptReport);
        acceptButton.setVisibility(View.GONE);
        CloudStoreUtil.updateCompanyReport(report);
        CloudStoreUtil.getOwner(report.getOwnerUuid(), new CloudStoreUtil.OwnerCallback() {
            @Override
            public void onSuccess(Owner myItem) {
                myItem.setBlocked(true);
                CloudStoreUtil.blockOwner(myItem);
                CloudStoreUtil.getEmployeesList(myItem.getUserUID(), new CloudStoreUtil.EmployeesListCallback() {
                    @Override
                    public void onSuccess(ArrayList<Employee> itemList) {
                        for(Employee e: itemList){
                            e.setBlocked(true);
                            CloudStoreUtil.blockEmployee(e);
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
                System.err.println("Error fetching document: " + e.getMessage());
            }
        });
        disableOwnerPackages(report.getOwnerUuid());
        disableOwnerProducts(report.getOwnerUuid());
        disableOwnerServices(report.getOwnerUuid());
        cancelOwnerReservations(report.getOwnerUuid(), report.getCompanyName());
    }
    private void cancelOwnerReservations(String ownerId, String companyName){
        CloudStoreUtil.getServieReservationsList(new CloudStoreUtil.ServiceReservationsListCallback() {
            @Override
            public void onSuccess(ArrayList<EmployeeReservation> itemList) {
                itemList.removeIf(r -> r.getStatus() != EmployeeReservation.ReservationStatus.New);
                for(EmployeeReservation reservation : itemList){
                    CloudStoreUtil.getServiceWithRefId(reservation.getServiceRefId(), new CloudStoreUtil.ServiceByRefIdCallback() {
                        @Override
                        public void onSuccess(Service item) {
                            if(item.getOwnerUuid().equals(ownerId)){
                                reservation.setStatus(EmployeeReservation.ReservationStatus.CanceledByAdmin);
                                CloudStoreUtil.updateReservationStatus(reservation);
                                OurNotification not = new OurNotification(reservation.getEventOrganizerEmail(), "Reservation canceled",
                                        "Reservation from "+companyName+ " owner canceled", "notRead");
                                CloudStoreUtil.insertNotification(not);
                            }
                        }
                        @Override
                        public void onFailure(Exception e) {
                            System.err.println("Error fetching documents: " + e.getMessage());
                        }
                    });
                }
            }
            @Override
            public void onFailure(Exception e) {
                System.err.println("Error fetching documents: " + e.getMessage());
            }
        });
    }
    private void disableOwnerPackages(String userId){
        CloudStoreUtil.selectPackages(new CloudStoreUtil.PackageCallback(){
            @Override
            public void onCallbackPackage(ArrayList<Package> retrievedPackages) {
                retrievedPackages.removeIf(p -> !p.getOwnerUuid().equals(userId));
                for(Package p: retrievedPackages){
                    CloudStoreUtil.disablePackage(p);
                }
            }
        });
    }

    private void disableOwnerServices(String userId){
        CloudStoreUtil.selectServices(new CloudStoreUtil.ServiceCallback(){
            @Override
            public void onCallbackService(ArrayList<Service> retrievedServices) {
                retrievedServices.removeIf(p -> !p.getOwnerUuid().equals(userId));
                for(Service s: retrievedServices){
                    CloudStoreUtil.disableService(s);
                }
            }
        });
    }

    private void disableOwnerProducts(String userId){
        CloudStoreUtil.selectProducts(new CloudStoreUtil.ProductCallback(){
            @Override
            public void onCallback(ArrayList<Product> retreivedProducts) {
                retreivedProducts.removeIf(p -> !p.getOwnerUuid().equals(userId));
                for(Product p: retreivedProducts){
                    CloudStoreUtil.disableProduct(p);
                }
            }
        });
    }

    private void openOwnerProfile(String id){
        ProfilePageData profileData = new ProfilePageData();
        profileData.setRole(0);
        profileData.setUserId(id);
        FragmentTransition.to(ProfilePageFragment.newInstance(profileData), activity,
                true, R.id.scroll_company_reports_list, "profilePage");
    }

    private void openOrganizerProfile(String id){
        ProfilePageData profileData = new ProfilePageData();
        profileData.setRole(1);
        profileData.setUserId(id);
        FragmentTransition.to(ProfilePageFragment.newInstance(profileData), activity,
                true, R.id.scroll_company_reports_list, "profilePage");
    }
}
