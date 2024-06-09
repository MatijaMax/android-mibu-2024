package com.example.ma02mibu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.model.CompanyGrade;
import com.example.ma02mibu.model.CompanyGradeReport;
import com.example.ma02mibu.model.OurNotification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CompanyGradeReportsAdapter extends ArrayAdapter<CompanyGradeReport> {
    private final ArrayList<CompanyGradeReport> companyGradeReports;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FragmentActivity currFragActivity;

    public CompanyGradeReportsAdapter(Context context, ArrayList<CompanyGradeReport> data) {
        super(context, R.layout.company_grade_report_view, data);
        companyGradeReports = data;
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
    }

    private LayoutInflater inflater;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CompanyGradeReportsAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.company_grade_report_view, parent, false);
            holder = new CompanyGradeReportsAdapter.ViewHolder();
            holder.gradeTextView = convertView.findViewById(R.id.text_grade);
            holder.commentTextView = convertView.findViewById(R.id.text_comment);holder.gradeTextView = convertView.findViewById(R.id.text_grade);
            holder.organizersEmailTextView = convertView.findViewById(R.id.text_owners_email);
            holder.createdDateTextView = convertView.findViewById(R.id.text_reported_date);
            holder.rejectButton = convertView.findViewById(R.id.btn_reject);
            holder.acceptButton = convertView.findViewById(R.id.btn_accept);
            holder.reportedReasonTextView = convertView.findViewById(R.id.text_reason);
            holder.rejectReasonEditView = convertView.findViewById(R.id.text_reason_rejected);
            holder.reportStatusTextView = convertView.findViewById(R.id.text_reported_status);
            convertView.setTag(holder);
        } else {
            holder = (CompanyGradeReportsAdapter.ViewHolder) convertView.getTag();
        }


        CompanyGradeReport report = getItem(position);

        if(report.getReportStatus() != CompanyGradeReport.REPORTSTATUS.REPORTED){
            holder.acceptButton.setVisibility(View.GONE);
            holder.rejectButton.setVisibility(View.GONE);
            holder.rejectReasonEditView.setVisibility(View.GONE);
        }

        holder.gradeTextView.setText(String.valueOf(report.getGrade()));
        holder.commentTextView.setText(report.getComment());
        holder.organizersEmailTextView.setText("Owners email: " + report.getOwnersEmail());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedDate = dateFormat.format(report.getReportedDate());
        holder.createdDateTextView.setText("Reported date: " + formattedDate);
        holder.reportStatusTextView.setText("Status: " + report.getReportStatus().toString());
        holder.reportedReasonTextView.setText("Report reason: "+report.getReason());
        holder.acceptButton.setOnClickListener(view -> {
            report.setReportStatus(CompanyGradeReport.REPORTSTATUS.ACCEPTED);
            CompanyGrade companyGrade = new CompanyGrade(report.getGrade(), "String comment", "String ownerRefId", new Date(), "String organizersEmail", report.getCompanyGradeUID(), true, true);
            CloudStoreUtil.updateCompanyGradeReport(report, new CloudStoreUtil.UpdateReadCallback() {
                @Override
                public void onSuccess() {
                    System.out.println("Item updated! TTTTTTTTTTTTTTTTTTTTTTTTTT");
                }
                @Override
                public void onFailure(Exception e) {
                    System.err.println("Error updating item: " + e.getMessage());
                }
            });
            CloudStoreUtil.updateCompanyGrade(companyGrade, new CloudStoreUtil.UpdateReadCallback() {
                @Override
                public void onSuccess() {
                    System.out.println("Item updated!");
                }
                @Override
                public void onFailure(Exception e) {
                    System.err.println("Error updating item: " + e.getMessage());
                }
            });
            holder.reportStatusTextView.setText("Status: " + report.getReportStatus().toString());
            holder.rejectButton.setVisibility(View.GONE);
            holder.acceptButton.setVisibility(View.GONE);
            holder.rejectReasonEditView.setVisibility(View.GONE);
        });

        holder.rejectButton.setOnClickListener(view -> {
            report.setReportStatus(CompanyGradeReport.REPORTSTATUS.REJECTED);
            CloudStoreUtil.updateCompanyGradeReport(report, new CloudStoreUtil.UpdateReadCallback() {
                @Override
                public void onSuccess() {
                    System.out.println("Item updated! TTTTTTTTTTTTTTTTTTTTTTTTTT");
                }
                @Override
                public void onFailure(Exception e) {
                    System.err.println("Error updating item: " + e.getMessage());
                }
            });
            OurNotification notification = new OurNotification(report.getOwnersEmail(), "Rejected report","Reason: " + holder.rejectReasonEditView.getText().toString(), "notRead");
            CloudStoreUtil.insertNotification(notification);
            holder.reportStatusTextView.setText("Status: " + report.getReportStatus().toString());
            holder.rejectButton.setVisibility(View.GONE);
            holder.acceptButton.setVisibility(View.GONE);
            holder.rejectReasonEditView.setVisibility(View.GONE);
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView gradeTextView;
        TextView commentTextView;
        TextView organizersEmailTextView;
        TextView createdDateTextView;
        TextView reportedReasonTextView;
        EditText rejectReasonEditView;
        TextView reportStatusTextView;
        Button rejectButton;
        Button acceptButton;
    }
}
