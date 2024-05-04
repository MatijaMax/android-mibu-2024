package com.example.ma02mibu.fragments.employees;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.databinding.FragmentEmployeeDetailsBinding;
import com.example.ma02mibu.databinding.FragmentEmployeeRegistrationBinding;
import com.example.ma02mibu.databinding.FragmentEmployeeWorkTimeEntryBinding;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.WorkSchedule;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmployeeWorkTimeEntryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployeeWorkTimeEntryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Employee mEmployee;
    private String ownerRefId;

    public EmployeeWorkTimeEntryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmployeeWorkTimeEntryFragment.
     */

    private FragmentEmployeeWorkTimeEntryBinding binding;
    public static EmployeeWorkTimeEntryFragment newInstance(Employee param1, String param2) {
        EmployeeWorkTimeEntryFragment fragment = new EmployeeWorkTimeEntryFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEmployeeWorkTimeEntryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        eventSelectedDayStart = view.findViewById(R.id.eventSelectedStart);
        Button btnPickStartDate = view.findViewById(R.id.btnNewEventStartDate);
        btnPickStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog2();
            }
        });
        eventSelectedDayEnd = view.findViewById(R.id.eventSelectedEnd);
        Button btnPickEndDate = view.findViewById(R.id.btnNewEventEndDate);
        btnPickEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog1();
            }
        });

        Button btnRegister = binding.btnRegister;
        btnRegister.setOnClickListener(v -> {
            try {
                addWorkingHours();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        return view;
    }

    private void addWorkingHours() throws InterruptedException{
        String mondayHours = binding.etMondayHours.getText().toString();
        String tuesdayHours = binding.etTuesdayHours.getText().toString();
        String wedHours = binding.etWednesdayHours.getText().toString();
        String thurHours = binding.etThursdayHours.getText().toString();
        String friHours = binding.etFridayHours.getText().toString();
        String satHours = binding.SaturdayHours.getText().toString();
        String sunHours = binding.etSundayHours.getText().toString();
        if(mondayHours.isEmpty() && tuesdayHours.isEmpty() && wedHours.isEmpty() && thurHours.isEmpty() && friHours.isEmpty()
                && satHours.isEmpty() && sunHours.isEmpty()){
            alertShow("All");
            return;
        }
        WorkSchedule customWorkSchedule = new WorkSchedule();
        if(!mondayHours.isEmpty()){
            String[] dayHM = extraxtHM(mondayHours);
            if(dayHM.length == 4){
                customWorkSchedule.setWorkTime(DayOfWeek.MONDAY, LocalTime.of(Integer.parseInt(dayHM[0]), Integer.parseInt(dayHM[1])), LocalTime.of(Integer.parseInt(dayHM[2]), Integer.parseInt(dayHM[3])));
            }else{
                alertShow("Monday");
                return;
            }
        }else{
            customWorkSchedule.setWorkTime(DayOfWeek.MONDAY, null, null);
        }
        if(!tuesdayHours.isEmpty()){
            String[] dayHM = extraxtHM(tuesdayHours);
            if(dayHM.length == 4){
                customWorkSchedule.setWorkTime(DayOfWeek.TUESDAY, LocalTime.of(Integer.parseInt(dayHM[0]), Integer.parseInt(dayHM[1])), LocalTime.of(Integer.parseInt(dayHM[2]), Integer.parseInt(dayHM[3])));
            }else{
                alertShow("Tuesday");
                return;
            }
        }else{
            customWorkSchedule.setWorkTime(DayOfWeek.TUESDAY, null, null);
        }
        if(!wedHours.isEmpty()){
            String[] dayHM = extraxtHM(wedHours);
            if(dayHM.length == 4){
                customWorkSchedule.setWorkTime(DayOfWeek.WEDNESDAY, LocalTime.of(Integer.parseInt(dayHM[0]), Integer.parseInt(dayHM[1])), LocalTime.of(Integer.parseInt(dayHM[2]), Integer.parseInt(dayHM[3])));
            }else{
                alertShow("Wednesday");
                return;
            }
        }else{
            customWorkSchedule.setWorkTime(DayOfWeek.WEDNESDAY, null, null);
        }
        if(!thurHours.isEmpty()){
            String[] dayHM = extraxtHM(thurHours);
            if(dayHM.length == 4){
                customWorkSchedule.setWorkTime(DayOfWeek.THURSDAY, LocalTime.of(Integer.parseInt(dayHM[0]), Integer.parseInt(dayHM[1])), LocalTime.of(Integer.parseInt(dayHM[2]), Integer.parseInt(dayHM[3])));
            }else{
                alertShow("Thursday");
                return;
            }
        }else{
            customWorkSchedule.setWorkTime(DayOfWeek.THURSDAY, null, null);
        }
        if(!friHours.isEmpty()){
            String[] dayHM = extraxtHM(friHours);
            if(dayHM.length == 4){
                customWorkSchedule.setWorkTime(DayOfWeek.FRIDAY, LocalTime.of(Integer.parseInt(dayHM[0]), Integer.parseInt(dayHM[1])), LocalTime.of(Integer.parseInt(dayHM[2]), Integer.parseInt(dayHM[3])));
            }else{
                alertShow("Friday");
                return;
            }
        }else{
            customWorkSchedule.setWorkTime(DayOfWeek.FRIDAY, null, null);
        }
        if(!satHours.isEmpty()){
            String[] dayHM = extraxtHM(satHours);
            if(dayHM.length == 4){
                customWorkSchedule.setWorkTime(DayOfWeek.SATURDAY, LocalTime.of(Integer.parseInt(dayHM[0]), Integer.parseInt(dayHM[1])), LocalTime.of(Integer.parseInt(dayHM[2]), Integer.parseInt(dayHM[3])));
            }else{
                alertShow("Saturday");
                return;
            }
        }else{
            customWorkSchedule.setWorkTime(DayOfWeek.SATURDAY, null, null);
        }
        if(!sunHours.isEmpty()){
            String[] dayHM = extraxtHM(sunHours);
            if(dayHM.length == 4){
                customWorkSchedule.setWorkTime(DayOfWeek.SUNDAY, LocalTime.of(Integer.parseInt(dayHM[0]), Integer.parseInt(dayHM[1])), LocalTime.of(Integer.parseInt(dayHM[2]), Integer.parseInt(dayHM[3])));
            }else{
                alertShow("Sunday");
                return;
            }
        }else{
            customWorkSchedule.setWorkTime(DayOfWeek.SUNDAY, null, null);
        }
        customWorkSchedule.setStartDay(eventSelectedDayStart.getText().toString());
        customWorkSchedule.setEndDay(eventSelectedDayEnd.getText().toString());
        mEmployee.setSchedule(customWorkSchedule);
        CloudStoreUtil.updateEmployeeWorkingHours(mEmployee, ownerRefId);
        Thread.sleep(600);
        FragmentTransition.to(EmployeeListFragment.newInstance(), getActivity(),
                true, R.id.scroll_employees_list, "EmployeeWTE");
    }

    private void alertShow(String day) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Wrong input");
        alertDialog.setMessage(day + " working hours are in wrong format! \nPlease use hh:MM-hh:MM or leave empty");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // No action needed here since the button does nothing.
            }
        });
        alertDialog.show();
    }

    private String[] extraxtHM(String hoursEntry){
        String start, end, startH, startM, endH, endM;
        String[] times = hoursEntry.split("-");
        if(times.length == 2){
            start = times[0];
            end = times[1];
        }else{
            return new String[]{};
        }
        String[] startHM = start.split(":");
        if(startHM.length == 2){
            startH = startHM[0];
            startM = startHM[1];
        }else{
            return new String[]{};
        }
        String[] endHM = end.split(":");
        if(endHM.length == 2){
            endH = endHM[0];
            endM = endHM[1];
        }else{
            return new String[]{};
        }
        return new String[]{startH, startM, endH, endM};
    }

    private TextView eventSelectedDayStart;
    private TextView eventSelectedDayEnd;
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
                        int monthN = month + 1;
                        String toSet = dayOfMonth + "-" + monthN + "-" + year;
                        // Display the selected week
                        eventSelectedDayEnd.setText(toSet);
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
                        String toSet = dayOfMonth + "-" + monthN + "-" + year;
                        // Display the selected week
//                        if(dayOfMonth < 10 && monthN < 10){
//                            toSet = "0"+dayOfMonth + "-" + "0"+monthN + "-" + year + " ";
//                        }else if(dayOfMonth < 10){
//                            toSet = "0"+dayOfMonth + "-" + monthN + "-" + year + " ";
//                        }else if(monthN < 10){
//                            toSet = dayOfMonth + "-" + "0"+monthN + "-" + year + " ";
//                        }else{
//                            toSet = dayOfMonth + "-" + monthN + "-" + year + " ";
//                        }
                        eventSelectedDayStart.setText(toSet);
                    }
                },
                year,
                month,
                dayOfMonth
        );

        datePickerDialog.show();
    }
}