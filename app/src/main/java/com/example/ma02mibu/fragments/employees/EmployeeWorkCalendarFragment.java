package com.example.ma02mibu.fragments.employees;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ma02mibu.R;
import com.example.ma02mibu.model.Employee;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmployeeWorkCalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployeeWorkCalendarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EmployeeWorkCalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmployeeWorkCalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmployeeWorkCalendarFragment newInstance(String param1, String param2) {
        EmployeeWorkCalendarFragment fragment = new EmployeeWorkCalendarFragment();
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
    private TextView tvSelectedWeek;
    private TextView eventSelectedDay;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_employee_work_calendar, container, false);
        tvSelectedWeek = view.findViewById(R.id.tvSelectedWeek);
        Button btnPickWeek = view.findViewById(R.id.btnPickWeek);
        btnPickWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        eventSelectedDay = view.findViewById(R.id.eventSelectedDate);
        Button btnPickEventDate = view.findViewById(R.id.btnNewEventDate);
        btnPickEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog2();
            }
        });
        return view;
    }
    private void showDatePickerDialog() {
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
                        int weekNumber = selectedCalendar.get(Calendar.WEEK_OF_YEAR);

                        // Display the selected week
                        tvSelectedWeek.setText(" number : " + weekNumber + " ");
                    }
                },
                year,
                month,
                dayOfMonth
        );

        datePickerDialog.show();
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
                        int selectedDate = selectedCalendar.get(Calendar.DAY_OF_MONTH);

                        // Display the selected week
                        eventSelectedDay.setText(dayOfMonth + "-" + month + "-" + year + " ");
                    }
                },
                year,
                month,
                dayOfMonth
        );

        datePickerDialog.show();
    }
}