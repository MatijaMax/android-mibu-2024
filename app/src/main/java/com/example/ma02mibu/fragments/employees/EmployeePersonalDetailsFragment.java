package com.example.ma02mibu.fragments.employees;

import android.os.Bundle;

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
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.databinding.FragmentEmployeeDetailsBinding;
import com.example.ma02mibu.databinding.FragmentEmployeePersonalDetailsBinding;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.Owner;
import com.example.ma02mibu.model.WorkSchedule;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmployeePersonalDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployeePersonalDetailsFragment extends Fragment {

    private FirebaseAuth auth;
    private String employeeRefId;


    private Employee mEmployee;
    private FragmentEmployeePersonalDetailsBinding binding;

    public EmployeePersonalDetailsFragment() {
        // Required empty public constructor
    }

    public static EmployeePersonalDetailsFragment newInstance() {
        EmployeePersonalDetailsFragment fragment = new EmployeePersonalDetailsFragment();
//        Bundle args = new Bundle();
//        args.putParcelable(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mEmployee = getArguments().getParcelable(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            employeeRefId = user.getUid();
        }
        binding = FragmentEmployeePersonalDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        CloudStoreUtil.getEmployee(employeeRefId, new CloudStoreUtil.EmployeeCallback() {
            @Override
            public void onSuccess(Employee myItem) {
                // Handle the retrieved item (e.g., display it in UI)
                System.out.println("Retrieved item: " + myItem);
                mEmployee = myItem;

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
                    FragmentTransition.to(EmployeePersonalWorkCalendarFragment.newInstance(mEmployee,""), getActivity(),
                            true, R.id.personal_employee, "EmployeePersonalWorkCalendar");
                });

            }

            @Override
            public void onFailure(Exception e) {
                // Handle the failure (e.g., show an error message)
                System.err.println("Error fetching document: " + e.getMessage());
            }
        });
        return view;
    }
}