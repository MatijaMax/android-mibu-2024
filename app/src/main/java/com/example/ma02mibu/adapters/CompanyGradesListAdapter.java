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

public class CompanyGradesListAdapter extends ArrayAdapter<CompanyGrade> {
    private final ArrayList<CompanyGrade> companyGrades;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FragmentActivity currFragActivity;

    public CompanyGradesListAdapter(Context context, ArrayList<CompanyGrade> data) {
        super(context, R.layout.company_grade_view, data);
        companyGrades = data;
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
    }
    private LayoutInflater inflater;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.company_grade_view, parent, false);
            holder = new ViewHolder();
            holder.gradeTextView = convertView.findViewById(R.id.text_grade);
            holder.commentTextView = convertView.findViewById(R.id.text_comment);holder.gradeTextView = convertView.findViewById(R.id.text_grade);
            holder.organizersEmailTextView = convertView.findViewById(R.id.text_organizers_email);
            holder.createdDateTextView = convertView.findViewById(R.id.text_created_date);
            holder.rejectButton = convertView.findViewById(R.id.btn_reject);
            holder.reasonEditText = convertView.findViewById(R.id.edit_reason);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        View view = convertView;
//        if (view == null) {
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            view = inflater.inflate(R.layout.company_grade_view, null);
//        }

        CompanyGrade grade = getItem(position);

        holder.gradeTextView.setText(String.valueOf(grade.getGrade()));
        holder.commentTextView.setText(grade.getComment());
        holder.organizersEmailTextView.setText("Organizers Email: " + grade.getOrganizersEmail());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedDate = dateFormat.format(grade.getCreatedDate());
        holder.createdDateTextView.setText("Created Date: " + formattedDate);

        holder.rejectButton.setOnClickListener(view -> {
//            holder.reasonEditText.setVisibility(
//                    holder.reasonEditText.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE
//            );
            CompanyGradeReport companyGradeReport = new CompanyGradeReport(grade.getUuid(), holder.reasonEditText.getText().toString(), new Date(), CompanyGradeReport.REPORTSTATUS.REPORTED,
                    grade.getGrade(), grade.getComment(), currentUser.getEmail());
            CloudStoreUtil.insertCompanyGradeReport(companyGradeReport);
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView gradeTextView;
        TextView commentTextView;
        TextView organizersEmailTextView;
        TextView createdDateTextView;
        Button rejectButton;
        EditText reasonEditText;
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View view = convertView;
//        if (view == null) {
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            view = inflater.inflate(R.layout.company_grade_view, null);
//        }
//
//        CompanyGrade item = getItem(position);
//        if (item != null) {
//            TextView gradeTextView = view.findViewById(R.id.text_grade);
//            TextView commentTextView = view.findViewById(R.id.text_comment);
//            TextView ownerRefIdTextView = view.findViewById(R.id.text_owner_ref_id);
//            TextView organizersEmailTextView = view.findViewById(R.id.text_organizers_email);
//            TextView createdDateTextView = view.findViewById(R.id.text_created_date);
//
//            gradeTextView.setText("Grade: " + item.getGrade());
//            commentTextView.setText("Comment: " + item.getComment());
//            ownerRefIdTextView.setText("Owner Ref ID: " + item.getOwnerRefId());
//            organizersEmailTextView.setText("Organizers Email: " + item.getOrganizersEmail());
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//            String formattedDate = dateFormat.format(item.getCreatedDate());
//            createdDateTextView.setText("Created Date: " + formattedDate);
//        }
//
//        return view;
//    }
}
