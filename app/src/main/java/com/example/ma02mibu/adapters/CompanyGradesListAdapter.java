package com.example.ma02mibu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.ma02mibu.R;
import com.example.ma02mibu.model.CompanyGrade;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.company_grade_view, null);
        }

        CompanyGrade item = getItem(position);
        if (item != null) {
            TextView gradeTextView = view.findViewById(R.id.text_grade);
            TextView commentTextView = view.findViewById(R.id.text_comment);
            TextView ownerRefIdTextView = view.findViewById(R.id.text_owner_ref_id);
            TextView organizersEmailTextView = view.findViewById(R.id.text_organizers_email);
            TextView createdDateTextView = view.findViewById(R.id.text_created_date);

            gradeTextView.setText("Grade: " + item.getGrade());
            commentTextView.setText("Comment: " + item.getComment());
            ownerRefIdTextView.setText("Owner Ref ID: " + item.getOwnerRefId());
            organizersEmailTextView.setText("Organizers Email: " + item.getOrganizersEmail());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String formattedDate = dateFormat.format(item.getCreatedDate());
            createdDateTextView.setText("Created Date: " + formattedDate);
        }

        return view;
    }
}
