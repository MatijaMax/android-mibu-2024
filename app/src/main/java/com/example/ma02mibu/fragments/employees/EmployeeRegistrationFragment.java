package com.example.ma02mibu.fragments.employees;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.databinding.FragmentEmployeeRegistrationBinding;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.fragments.products.ProductsListFragment;
import com.example.ma02mibu.model.Company;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.Owner;
import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.model.WorkSchedule;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class EmployeeRegistrationFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String ownerRefId;
    private String companyRefId;

    public EmployeeRegistrationFragment() {
        // Required empty public constructor
    }

    public static EmployeeRegistrationFragment newInstance(String param1, String param2) {
        EmployeeRegistrationFragment fragment = new EmployeeRegistrationFragment();
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

    private FragmentEmployeeRegistrationBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEmployeeRegistrationBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Button btnSelectImage = binding.btnUploadImage;
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        Button btnRegister = binding.btnRegister;
        btnRegister.setOnClickListener(v -> {
            try {
                addEmployee();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        return view;
    }

    private void addEmployee() throws InterruptedException {
        String fName = binding.etFirstName.getText().toString();
        String lName = binding.etLastName.getText().toString();
        String email = binding.etEmail.getText().toString();
        String pass1 = binding.etPassword.getText().toString();
        String pass2 = binding.etPasswordAgain.getText().toString();
        if(!pass1.equals(pass2)){
            // Passwords don't match, show an alert dialog:
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Passwords Don't Match");
            alertDialog.setMessage("Please make sure the passwords match.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // No action needed here since the button does nothing.
                }
            });
            alertDialog.show();
            return;
        }
        String address = binding.etAddress.getText().toString();
        String phoneNr = binding.etPhoneNumber.getText().toString();
        Employee e = new Employee(1L, fName, lName, email, pass1, address, phoneNr, R.drawable.employee_avatar);
        WorkSchedule companyWorkSchedule = new WorkSchedule();
        companyWorkSchedule.setWorkTime(DayOfWeek.MONDAY, LocalTime.NOON, LocalTime.of(15,30));
        companyWorkSchedule.setWorkTime(DayOfWeek.TUESDAY, LocalTime.of(8,30), LocalTime.of(16,30));
        companyWorkSchedule.setWorkTime(DayOfWeek.WEDNESDAY, LocalTime.of(8,30), LocalTime.of(15,30));
        companyWorkSchedule.setWorkTime(DayOfWeek.THURSDAY, LocalTime.of(8,30), LocalTime.of(14,30));
        companyWorkSchedule.setWorkTime(DayOfWeek.FRIDAY, LocalTime.of(8,30), LocalTime.of(14,30));
        companyWorkSchedule.setWorkTime(DayOfWeek.SATURDAY, LocalTime.NOON, LocalTime.of(14,30));
        companyWorkSchedule.setWorkTime(DayOfWeek.SUNDAY, null, null);
        companyWorkSchedule.setStartDay(LocalDate.of(2024, 3, 14).toString());
        companyWorkSchedule.setEndDay(LocalDate.of(2024, 7, 22).toString());
        e.setSchedule(companyWorkSchedule);
        CloudStoreUtil.insertEmployee(e, "LpDHEOT9JWhVNrHWP20K");
        Thread.sleep(500);
        FragmentTransition.to(EmployeeListFragment.newInstance(), getActivity(),
                true, R.id.scroll_employees_list, "EmployeeRegistration");
    }

    private void chooseImage(){
        ownerRefId = CloudStoreUtil.insertOwner(new Owner("10", "PUPV"));
        WorkSchedule companyWorkSchedule = new WorkSchedule();
        companyWorkSchedule.setWorkTime(DayOfWeek.MONDAY, LocalTime.NOON, LocalTime.of(15,30));
        companyWorkSchedule.setWorkTime(DayOfWeek.TUESDAY, LocalTime.of(8,30), LocalTime.of(16,30));
        companyWorkSchedule.setWorkTime(DayOfWeek.WEDNESDAY, LocalTime.of(8,30), LocalTime.of(15,30));
        companyWorkSchedule.setWorkTime(DayOfWeek.THURSDAY, LocalTime.of(8,30), LocalTime.of(14,30));
        companyWorkSchedule.setWorkTime(DayOfWeek.FRIDAY, LocalTime.of(8,30), LocalTime.of(14,30));
        companyWorkSchedule.setWorkTime(DayOfWeek.SATURDAY, LocalTime.NOON, LocalTime.of(14,30));
        companyWorkSchedule.setWorkTime(DayOfWeek.SUNDAY, null, null);
        companyWorkSchedule.setStartDay(null);
        companyWorkSchedule.setEndDay(null);
        CloudStoreUtil.insertCompany(new Company("22", "KK", companyWorkSchedule), ownerRefId);
//        Intent i = new Intent();
//        i.setType("image/*");
//        i.setAction(Intent.ACTION_GET_CONTENT);
//
//        startActivity(i);
    }
}