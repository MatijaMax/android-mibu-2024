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
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ma02mibu.R;
import com.example.ma02mibu.adapters.EventExpandableListAdapter;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.EventModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmployeeWorkCalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployeeWorkCalendarFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Employee mEmployee;
    private String mParam2;

    public EmployeeWorkCalendarFragment() {
        // Required empty public constructor
    }

    public static EmployeeWorkCalendarFragment newInstance(Employee param1, String param2) {
        EmployeeWorkCalendarFragment fragment = new EmployeeWorkCalendarFragment();
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
    private TextView tvSelectedWeek;
    private TextView eventSelectedDay;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_employee_work_calendar, container, false);
//        ExpandableListView expandableListView = view.findViewById((R.id.expandableListView));
//        HashMap<String, List<EventModel>> expandableListDetail = new HashMap<String, List<EventModel>>();
//
//        List<EventModel> eventsList1 = new ArrayList<EventModel>();
//        eventsList1.add(new EventModel("dogadjaj1", LocalDate.of(2024, 4, 18), LocalTime.of(10,30), LocalTime.of(11,0),"booked"));
//        eventsList1.add(new EventModel("dogadjaj5", LocalDate.of(2024, 4, 18), LocalTime.of(12,30), LocalTime.of(14,20),"booked"));
//
//        List<EventModel> eventsList2 = new ArrayList<EventModel>();
//        eventsList2.add(new EventModel("dogadjaj3", LocalDate.of(2024, 4, 16), LocalTime.of(10,30), LocalTime.of(11,0),"booked"));
//        eventsList1.add(new EventModel("dogadjaj9", LocalDate.of(2024, 4, 16), LocalTime.of(12,30), LocalTime.of(14,20),"booked"));
//        eventsList2.add(new EventModel("dogadjaj7", LocalDate.of(2024, 4, 16), LocalTime.of(15,30), LocalTime.of(16,0),"booked"));
//
//        expandableListDetail.put("DAN1", eventsList1);
//        expandableListDetail.put("DAN2", eventsList2);
//        List<String> expandableListTitle;
//        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
//        EventExpandableListAdapter adapter = new EventExpandableListAdapter( getActivity(), expandableListTitle, expandableListDetail);
//        expandableListView.setAdapter(adapter);
        tvSelectedWeek = view.findViewById(R.id.tvSelectedWeek);
        Button btnPickWeek = view.findViewById(R.id.btnPickWeek);
        btnPickWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(view);
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
    private void showDatePickerDialog(View parentV) {
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
                        DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        int weekNumber = selectedCalendar.get(Calendar.WEEK_OF_YEAR);
                        tvSelectedWeek.setText(" number : " + weekNumber + " ");

                        selectedCalendar.setFirstDayOfWeek(Calendar.MONDAY);
                        selectedCalendar.set(Calendar.DAY_OF_WEEK, selectedCalendar.getFirstDayOfWeek());

                        ExpandableListView expandableListView = parentV.findViewById((R.id.expandableListView));
                        HashMap<String, List<EventModel>> expandableListDetail = new HashMap<String, List<EventModel>>();

                        //monday
                        List<EventModel> eventsList1 = new ArrayList<EventModel>();
                        eventsList1.add(new EventModel("dogadjaj1", LocalDate.of(2024, 4, 18), LocalTime.of(10,30), LocalTime.of(11,0),"taken"));
                        eventsList1.add(new EventModel("dogadjaj5", LocalDate.of(2024, 4, 18), LocalTime.of(12,30), LocalTime.of(14,20),"taken"));

                        expandableListDetail.put(df.format(selectedCalendar.getTime()) + "\t    " + mEmployee.findActiveWorkSchedule().ScheduleForDay(DayOfWeek.MONDAY), eventsList1);

                        //tuesday
                        selectedCalendar.add(Calendar.DAY_OF_MONTH, 1);
                        List<EventModel> eventsList2 = new ArrayList<EventModel>();
                        eventsList2.add(new EventModel("dogadjaj1", LocalDate.of(2024, 4, 18), LocalTime.of(10,30), LocalTime.of(11,0),"taken"));
                        eventsList2.add(new EventModel("dogadjaj5", LocalDate.of(2024, 4, 18), LocalTime.of(12,30), LocalTime.of(14,20),"booked"));

                        expandableListDetail.put(df.format(selectedCalendar.getTime())  + "\t    " + mEmployee.findActiveWorkSchedule().ScheduleForDay(DayOfWeek.TUESDAY), eventsList2);

                        //wed
                        selectedCalendar.add(Calendar.DAY_OF_MONTH, 1);
                        List<EventModel> eventsList3 = new ArrayList<EventModel>();
                        eventsList3.add(new EventModel("dogadjaj1", LocalDate.of(2024, 4, 18), LocalTime.of(10,30), LocalTime.of(11,0),"booked"));
                        eventsList3.add(new EventModel("dogadjaj5", LocalDate.of(2024, 4, 18), LocalTime.of(12,30), LocalTime.of(14,20),"taken"));

                        expandableListDetail.put(df.format(selectedCalendar.getTime())  + "\t    " + mEmployee.findActiveWorkSchedule().ScheduleForDay(DayOfWeek.WEDNESDAY), eventsList3);

                        //thur
                        selectedCalendar.add(Calendar.DAY_OF_MONTH, 1);
                        List<EventModel> eventsList4 = new ArrayList<EventModel>();
                        eventsList4.add(new EventModel("dogadjaj1", LocalDate.of(2024, 4, 18), LocalTime.of(10,30), LocalTime.of(11,0),"booked"));
                        eventsList4.add(new EventModel("dogadjaj5", LocalDate.of(2024, 4, 18), LocalTime.of(12,30), LocalTime.of(14,20),"taken"));

                        expandableListDetail.put(df.format(selectedCalendar.getTime())  + "\t    " + mEmployee.findActiveWorkSchedule().ScheduleForDay(DayOfWeek.THURSDAY), eventsList4);

                        //friday
                        selectedCalendar.add(Calendar.DAY_OF_MONTH, 1);
                        List<EventModel> eventsList5 = new ArrayList<EventModel>();
                        eventsList5.add(new EventModel("dogadjaj1", LocalDate.of(2024, 4, 18), LocalTime.of(10,30), LocalTime.of(11,0),"booked"));
                        eventsList5.add(new EventModel("dogadjaj5", LocalDate.of(2024, 4, 18), LocalTime.of(12,30), LocalTime.of(14,20),"taken"));

                        expandableListDetail.put(df.format(selectedCalendar.getTime())  + "\t    " + mEmployee.findActiveWorkSchedule().ScheduleForDay(DayOfWeek.FRIDAY), eventsList5);

                        //sat
                        selectedCalendar.add(Calendar.DAY_OF_MONTH, 1);
                        List<EventModel> eventsList6 = new ArrayList<EventModel>();
                        eventsList6.add(new EventModel("dogadjaj1", LocalDate.of(2024, 4, 18), LocalTime.of(10,30), LocalTime.of(11,0),"booked"));

                        expandableListDetail.put(df.format(selectedCalendar.getTime())  + "\t    " + mEmployee.findActiveWorkSchedule().ScheduleForDay(DayOfWeek.SATURDAY), eventsList6);

                        //sun
                        selectedCalendar.add(Calendar.DAY_OF_MONTH, 1);
                        List<EventModel> eventsList7 = new ArrayList<EventModel>();

                        expandableListDetail.put(df.format(selectedCalendar.getTime())  + "\t    " + mEmployee.findActiveWorkSchedule().ScheduleForDay(DayOfWeek.SUNDAY), eventsList7);

                        List<String> expandableListTitle;
                        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                        Collections.sort(expandableListTitle);
                        EventExpandableListAdapter adapter = new EventExpandableListAdapter( getActivity(), expandableListTitle, expandableListDetail);
                        expandableListView.setAdapter(adapter);
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
                        int monthN = month + 1;
                        // Display the selected week
                        eventSelectedDay.setText(dayOfMonth + "-" + monthN + "-" + year + " ");
                    }
                },
                year,
                month,
                dayOfMonth
        );

        datePickerDialog.show();
    }
}