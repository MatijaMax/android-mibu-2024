package com.example.ma02mibu.fragments.employees;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.databinding.FragmentEmployeeDetailsBinding;
import com.example.ma02mibu.databinding.FragmentEmployeeListBinding;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.WorkSchedule;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmployeeDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployeeDetailsFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Employee mEmployee;
    private String ownerRefId;
    private FragmentEmployeeDetailsBinding binding;

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
            ownerRefId = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEmployeeDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
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
//        TextView textMonday = view.findViewById(R.id.workingTable);
//        StringBuilder working = new StringBuilder();
//        for (WorkSchedule workSchedule: mEmployee.getWorkSchedules()) {
//            working.append(workSchedule.toString()).append("\n\n");
//        }
//        textMonday.setText(working);
        ArrayList<String> workingHours = new ArrayList<String>();
        for (WorkSchedule workSchedule: mEmployee.getWorkSchedules()) {
            workingHours.add(workSchedule.toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, workingHours);
        ListView listView = view.findViewById(R.id.listWorking);
        listView.setAdapter(adapter);
        Button newEmployeeButton = binding.btnAddNewWorkTime;
        newEmployeeButton.setOnClickListener(v -> {
            FragmentTransition.to(EmployeeWorkTimeEntryFragment.newInstance(mEmployee,ownerRefId), getActivity(),
                    true, R.id.scroll_employees_list, "newWorkTimePage");
        });
        return view;
    }
}