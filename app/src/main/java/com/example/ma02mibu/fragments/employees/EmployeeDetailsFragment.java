package com.example.ma02mibu.fragments.employees;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ma02mibu.R;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.WorkSchedule;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmployeeDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployeeDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Employee mEmployee;
    private String mParam2;

    public EmployeeDetailsFragment() {
        // Required empty public constructor
    }
    public static EmployeeDetailsFragment newInstance(Employee param1, String param2) {
        EmployeeDetailsFragment fragment = new EmployeeDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEmployee = getArguments().getParcelable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_employee_details, container, false);
        TextView textFName = view.findViewById(R.id.firstNameTextView);
        textFName.setText(mEmployee.getFirstName());
        TextView textLName = view.findViewById(R.id.lastNameTextView);
        textLName.setText(mEmployee.getLastName());
        TextView textEmail = view.findViewById(R.id.emailText);
        textEmail.setText(mEmployee.getEmail());
//        TextView textPass = view.findViewById(R.id.passwordText);
//        textPass.setText(mEmployee.getPassword());
        TextView texAddress = view.findViewById(R.id.addressText);
        texAddress.setText(mEmployee.getAddress());
        TextView textPhone = view.findViewById(R.id.phoneNumberText);
        textPhone.setText(mEmployee.getPhoneNumber());
        ImageView imageView = view.findViewById(R.id.employeeImageView);
        imageView.setImageResource(mEmployee.getImage());
        TextView textMonday = view.findViewById(R.id.workingTable);
        for (WorkSchedule workSchedule: mEmployee.getWorkSchedules()) {
            textMonday.setText(workSchedule.toString());
        }
        return view;
    }
}