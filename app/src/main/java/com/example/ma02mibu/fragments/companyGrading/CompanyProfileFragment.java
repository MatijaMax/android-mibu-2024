package com.example.ma02mibu.fragments.companyGrading;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.CompanyGradesListAdapter;
import com.example.ma02mibu.adapters.EmployeeListAdapter;
import com.example.ma02mibu.databinding.FragmentEmployeeListBinding;
import com.example.ma02mibu.model.CompanyGrade;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.EmployeeReservation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompanyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth auth;
    private ArrayList<CompanyGrade> companyGrades;
    private String ownerRefId;

    private TextView eventSelectedDateStart;
    private TextView eventSelectedDateEnd;

    private Date startDate;
    private Date endDate;

    public CompanyProfileFragment() {
        // Required empty public constructor
    }
    private ListView listView;
    private CompanyGradesListAdapter adapter;
    public static CompanyProfileFragment newInstance(String param1, String param2) {
        CompanyProfileFragment fragment = new CompanyProfileFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            ownerRefId = user.getUid();
        }
        View view = inflater.inflate(R.layout.fragment_company_profile, container, false);

        listView = view.findViewById(R.id.list_view);
        startDate = null;
        endDate = null;

        loadGrades();

        Button btnFilter = view.findViewById(R.id.filterButton);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadGrades();
            }
        });

        eventSelectedDateStart = view.findViewById(R.id.eventSelectedDateStart);
        Button btnPickEventDate = view.findViewById(R.id.btnNewEventDateStart);
        btnPickEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog2();
            }
        });

        eventSelectedDateEnd = view.findViewById(R.id.eventSelectedDateEnd);
        Button btnPickEventDateEnd = view.findViewById(R.id.btnNewEventDateEnd);
        btnPickEventDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog1();
            }
        });

        return view;
    }

    private void showDatePickerDialog2() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Convert selected date to week number
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(year, month, dayOfMonth);
                        startDate = selectedCalendar.getTime();
                        int monthN = month + 1;
                        if(monthN < 10){
                            if(dayOfMonth < 10){
                                eventSelectedDateStart.setText("0" + dayOfMonth + "-" + "0" + monthN + "-" + year);
                            }else {
                                eventSelectedDateStart.setText(dayOfMonth + "-" + "0" + monthN + "-" + year);
                            }
                        }else{
                            if(dayOfMonth < 10){
                                eventSelectedDateStart.setText("0" + dayOfMonth + "-" + monthN + "-" + year);
                            }else{
                                eventSelectedDateStart.setText(dayOfMonth + "-" + monthN + "-" + year);
                            }
                        }
                    }
                },
                year,
                month,
                dayOfMonth
        );

        datePickerDialog.show();
    }

    private void showDatePickerDialog1() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Convert selected date to week number
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(year, month, dayOfMonth);
                        endDate = selectedCalendar.getTime();
                        int monthN = month + 1;
                        if(monthN < 10){
                            if(dayOfMonth < 10){
                                eventSelectedDateEnd.setText("0" + dayOfMonth + "-" + "0" + monthN + "-" + year);
                            }else {
                                eventSelectedDateEnd.setText(dayOfMonth + "-" + "0" + monthN + "-" + year);
                            }
                        }else{
                            if(dayOfMonth < 10){
                                eventSelectedDateEnd.setText("0" + dayOfMonth + "-" + monthN + "-" + year);
                            }else{
                                eventSelectedDateEnd.setText(dayOfMonth + "-" + monthN + "-" + year);
                            }
                        }
                    }
                },
                year,
                month,
                dayOfMonth
        );

        datePickerDialog.show();
    }

    private void loadGrades() {
        CloudStoreUtil.getCompanyGradesList(ownerRefId, new CloudStoreUtil.GradesListCallback() {
            @Override
            public void onSuccess(ArrayList<CompanyGrade> itemList) {
                companyGrades = new ArrayList<>(itemList);
                companyGrades.removeIf(cg -> cg.isDeleted());
                if(startDate != null){
                    companyGrades.removeIf(cg -> cg.getCreatedDate().compareTo(startDate) < 0);
                }
                if(endDate != null){
                    companyGrades.removeIf(cg -> cg.getCreatedDate().compareTo(endDate) > 0);
                }
                adapter = new CompanyGradesListAdapter(getActivity(), companyGrades);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Exception e) {
                // Handle the failure (e.g., show an error message)
                System.err.println("Error fetching documents: " + e.getMessage());
            }
        });
    }
}