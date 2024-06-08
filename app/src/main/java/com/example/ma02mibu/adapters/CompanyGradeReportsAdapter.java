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
            holder.rejectedReasonTextView = convertView.findViewById(R.id.text_reason);
            holder.reportStatusTextView = convertView.findViewById(R.id.text_reported_status);
            convertView.setTag(holder);
        } else {
            holder = (CompanyGradeReportsAdapter.ViewHolder) convertView.getTag();
        }

//        View view = convertView;
//        if (view == null) {
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            view = inflater.inflate(R.layout.company_grade_view, null);
//        }

        CompanyGradeReport report = getItem(position);

        holder.gradeTextView.setText(String.valueOf(report.getGrade()));
        holder.commentTextView.setText(report.getComment());
        holder.organizersEmailTextView.setText("Owners email: " + report.getOwnersEmail());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedDate = dateFormat.format(report.getReportedDate());
        holder.createdDateTextView.setText("Reported date: " + formattedDate);
        holder.reportStatusTextView.setText("Status: " + report.getReportstatus().toString());
        holder.rejectedReasonTextView.setText("Report reason: "+report.getReason());
        holder.rejectButton.setOnClickListener(view -> {
//            holder.reasonEditText.setVisibility(
//                    holder.reasonEditText.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE
//            );
//            CompanyGradeReport companyGradeReport = new CompanyGradeReport(grade.getUuid(), holder.reasonEditText.getText().toString(), new Date(), CompanyGradeReport.REPORTSTATUS.REPORTED);
//            CloudStoreUtil.insertCompanyGradeReport(companyGradeReport);
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView gradeTextView;
        TextView commentTextView;
        TextView organizersEmailTextView;
        TextView createdDateTextView;
        TextView rejectedReasonTextView;
        TextView reportStatusTextView;
        Button rejectButton;
        Button acceptButton;
    }
}
