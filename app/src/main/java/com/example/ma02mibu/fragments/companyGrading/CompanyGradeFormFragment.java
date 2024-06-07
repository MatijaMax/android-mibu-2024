package com.example.ma02mibu.fragments.companyGrading;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.databinding.FragmentCompanyGradeFormBinding;
import com.example.ma02mibu.databinding.FragmentEmployeeDetailsBinding;
import com.example.ma02mibu.model.CompanyGrade;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompanyGradeFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyGradeFormFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private NumberPicker numberPickerGrade;
    private EditText editTextComment;
    private Button buttonSubmit;
    private FirebaseAuth auth;
    private String ownerRefId = "dsd";
    private String organizerEmail;
    private FragmentCompanyGradeFormBinding binding;

    public CompanyGradeFormFragment() {
        // Required empty public constructor
    }

    public static CompanyGradeFormFragment newInstance(String param1, String param2) {
        CompanyGradeFormFragment fragment = new CompanyGradeFormFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            organizerEmail = user.getEmail();
        }
        binding = FragmentCompanyGradeFormBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        // Inflate the layout for this fragment
        numberPickerGrade = binding.numberPickerGrade;
        editTextComment = binding.editTextComment;
        buttonSubmit = binding.buttonSubmit;

        // Set the minimum and maximum values for the NumberPicker
        numberPickerGrade.setMinValue(1);
        numberPickerGrade.setMaxValue(5);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
        return view;
    }

    private void submitForm() {
        int grade = numberPickerGrade.getValue();
        String comment = editTextComment.getText().toString().trim();
        CompanyGrade companyGrade = new CompanyGrade(grade, comment, ownerRefId, new Date(), organizerEmail);

        CloudStoreUtil.insertCompanyGrade(companyGrade);
        String message = "Graded!";
        Toast.makeText(binding.getRoot().getContext() , message, Toast.LENGTH_SHORT).show();
    }
}