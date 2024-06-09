package com.example.ma02mibu.fragments.events;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.EmployeePickupListAdapter;
import com.example.ma02mibu.adapters.EmployeeTimeTableAdapter;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.EmployeeInService;
import com.example.ma02mibu.model.EmployeeReservation;
import com.example.ma02mibu.model.EventModel;
import com.example.ma02mibu.model.Package;
import com.example.ma02mibu.model.ProductDAO;
import com.example.ma02mibu.model.Service;
import com.example.ma02mibu.model.OurNotification;
import com.google.firebase.auth.FirebaseAuth;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class BuyProductFragment extends Fragment {

    private static final String ARG_PARAM1 = "product";
    private static final String ARG_PARAM2 = "package";
    private Service service;
    private Package aPackage = null;
    int i = 0;
    private ProductDAO product;

    private EmployeePickupListAdapter adapter;
    private EmployeeTimeTableAdapter timeTableAdapter;

    private ArrayList<EmployeeInService> employees;

    private ListView employeeListView;
    private ListView timeTableListView;
    private TextView selectedDateTextView;
    private TextView workingTime;

    private EditText fromEditText;
    private EditText toEditText;


    private EmployeeInService selectedEmployee;
    private Employee employee;
    private LocalDate selectedDate;


    public BuyProductFragment() { }

    public static BuyProductFragment newInstance(ProductDAO param1) {
        BuyProductFragment fragment = new BuyProductFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        args.putParcelable(ARG_PARAM2, null);
        fragment.setArguments(args);
        return fragment;
    }

    public static BuyProductFragment newInstance(ProductDAO param1, Package aPackage) {
        BuyProductFragment fragment = new BuyProductFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        args.putParcelable(ARG_PARAM2, aPackage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = getArguments().getParcelable(ARG_PARAM1);
            aPackage = getArguments().getParcelable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy_product, container, false);

        workingTime = view.findViewById(R.id.workingTimeTextView);
        selectedDateTextView = view.findViewById(R.id.selectedDateTextView);
        timeTableListView = view.findViewById(R.id.timetableListView);

        fromEditText = view.findViewById(R.id.fromEditText);
        toEditText = view.findViewById(R.id.toEditText);

        view.findViewById(R.id.finishButton).setOnClickListener(v -> {
            handleFinishClick();
        });

        view.findViewById(R.id.selectDate).setOnClickListener(this::showDatePicker);

        employeeListView = view.findViewById(R.id.selectEmployeeList);
        employeeListView.setOnItemClickListener((parent, view1, position, id) -> {
            selectedEmployee = adapter.getItem(position);
            getEmployee();

            updateTimeTable();
        });

        if(aPackage == null){
            getService(product.getDocumentRefId());
        } else {
            getService(aPackage.getServices().get(i).getFirestoreId());
        }

        return view;
    }

    private void handleFinishClick() {
        if(workingTime.getText().equals("not working")){
            Toast.makeText(getContext(), "Employee is not working this day", Toast.LENGTH_SHORT).show();
            return;
        }
        if(fromEditText.getText().toString().isEmpty() || toEditText.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Please insert from and to hour and minute values", Toast.LENGTH_SHORT).show();
        }

        //Checking if to time is before from time
        int[] temp;
        temp = extraxtHM(String.valueOf(fromEditText.getText()));
        int fromHour, fromMinute;
        fromHour = temp[0];
        fromMinute = temp[1];
        temp = extraxtHM(String.valueOf(toEditText.getText()));
        int toHour, toMinute;
        toHour = temp[0];
        toMinute = temp[1];

        if(!isBefore(fromHour, fromMinute, toHour, toMinute, false)){
            Toast.makeText(getContext(), "To time must be greater than from time", Toast.LENGTH_SHORT).show();
            return;
        }

        //Checking if employee is free
        for(int i = 0; i < timeTableAdapter.getCount(); i++){
            String time = timeTableAdapter.getItem(i);
            String[] temp2 = time.split("-");
            int occupiedFromHour, occupiedFromMinute;
            int[] temp3 = extraxtHM(temp2[0]);
            occupiedFromHour = temp3[0];
            occupiedFromMinute = temp3[1];

            int occupiedToHour, occupiedToMinute;
            temp3 = extraxtHM(temp2[1]);
            occupiedToHour = temp3[0];
            occupiedToMinute = temp3[1];

            if(isInside(fromHour, fromMinute, occupiedFromHour, occupiedFromMinute, occupiedToHour, occupiedToMinute) ||
                    isInside(toHour, toMinute, occupiedFromHour, occupiedFromMinute, occupiedToHour, occupiedToMinute) ||
                        isInside(occupiedFromHour, occupiedFromMinute, fromHour, fromMinute, toHour, toMinute) ||
                            isInside(occupiedToHour, occupiedToMinute, fromHour, fromMinute, toHour, toMinute)){
                Toast.makeText(getContext(), "Employee is occupied in that time frame, please look at the table", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        //Checking employee working time
        String[] temp2 = workingTime.getText().toString().split("-");
        temp = extraxtHM(temp2[0]);
        int workingFromHours, workingFromMinutes;
        int[] temp3 = extraxtHM(temp2[0]);
        workingFromHours = temp3[0];
        workingFromMinutes = temp3[1];
        int workingToHours, workingToMinutes;
        temp3 = extraxtHM(temp2[1]);
        workingToHours = temp3[0];
        workingToMinutes = temp3[1];

        if(!isInside(fromHour, fromMinute, workingFromHours, workingFromMinutes, workingToHours, workingToMinutes) ||
            !isInside(toHour, toMinute, workingFromHours, workingFromMinutes, workingToHours, workingToMinutes)){
            Toast.makeText(getContext(), "Employee is not working in this time frame", Toast.LENGTH_SHORT).show();
            return;
        }

        //Checking minimum and maximum duration
        int durationHours, durationMinutes;
        durationHours = toHour - fromHour;
        durationMinutes = toMinute - fromMinute;
        if (durationMinutes < 0) {
            durationMinutes += 60;
            durationHours -= 1;
        }
        if(!isInside(durationHours, durationMinutes, service.getMinHourDuration(), service.getMinMinutesDuration(), service.getMaxHourDuration(), service.getMaxMinutesDuration())){
            Toast.makeText(getContext(), "Duration must be " + service.getMinHourDuration() + ":" + service.getMinMinutesDuration() + "-" + service.getMaxHourDuration() + ":" + service.getMaxMinutesDuration(), Toast.LENGTH_SHORT).show();
            return;
        }

        Date start = Date.from(selectedDate.atTime(fromHour, fromMinute).atZone(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(selectedDate.atTime(toHour, toMinute).atZone(ZoneId.systemDefault()).toInstant());
        EmployeeReservation reservation = new EmployeeReservation(selectedEmployee.getEmail(),
                                                                    FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                                                    service.getFirestoreId(),
                                                                    start,
                                                                    end,
                                                                    null,
                                                                    EmployeeReservation.ReservationStatus.New);

        CloudStoreUtil.insertServiceReservation(reservation);

        OurNotification employeeNotification = new OurNotification(selectedEmployee.getEmail(),
                                                                    "New service reservation",
                                                                    FirebaseAuth.getInstance().getCurrentUser().getEmail() + " created service reservation",
                                                                    "notRead");
        CloudStoreUtil.insertNotification(employeeNotification);

        Toast.makeText(getContext(), "Nice, sve je ok", Toast.LENGTH_SHORT).show();

        if(aPackage == null){
            FragmentTransition.to(SelectEventFragment.newInstance(), getActivity(), true, R.id.products_container, "productsManagement");
        } else {
            i += 1;
            if(i == aPackage.getServices().size()){
                FragmentTransition.to(SelectEventFragment.newInstance(), getActivity(), true, R.id.products_container, "productsManagement");
            } else {
                getService(aPackage.getServices().get(i).getFirestoreId());
                selectedEmployee = null;
                employee = null;
                workingTime.setText("Employee working time");
                timeTableAdapter = new EmployeeTimeTableAdapter(getContext(), new ArrayList<>());
                timeTableListView.setAdapter(timeTableAdapter);
            }
        }
    }


    private void getService(String refId) {
        CloudStoreUtil.selectService(refId, result -> {
            service = result;
            employees = service.getPersons();
            adapter = new EmployeePickupListAdapter(getContext(), employees, getActivity());
            employeeListView.setAdapter(adapter);
        });
    }

    private void showDatePicker(View parentV) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                (view, year1, month1, dayOfMonth1) -> {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(year1, month1, dayOfMonth1);

                    if(LocalDate.of(year1, month1+1, dayOfMonth1).isAfter(LocalDate.now())){
                        selectedDate = LocalDate.of(year1, month1+1, dayOfMonth1);
                        selectedDateTextView.setText(dayOfMonth1 + "-" + (month1 + 1) + "-" + year1);
                    }else {
                        Toast.makeText(getContext(), "Select date that is in the future", Toast.LENGTH_SHORT).show();
                    }

                    updateTimeTable();
                },
                year,
                month,
                dayOfMonth
        );

        datePickerDialog.show();
    }

    private void updateTimeTable(){
        if(selectedDate == null || selectedEmployee == null || employee == null || employee.getWorkSchedules() == null){return;}

        workingTime.setText(employee.getWorkSchedules().get(0).ScheduleForDay(selectedDate.getDayOfWeek()));

        if(Objects.equals(employee.getWorkSchedules().get(0).ScheduleForDay(selectedDate.getDayOfWeek()), "not working")){
            return;
        }

        String day = String.valueOf(selectedDate.getDayOfMonth());
        if(day.length() < 2){
            day = "0" + day;
        }

        String month = String.valueOf(selectedDate.getMonthValue());
        if(month.length() < 2){
            month = "0" + month;
        }

        String year = String.valueOf(selectedDate.getYear());
        CloudStoreUtil.getEventModels(selectedEmployee.getEmail(), day+"-"+month+"-"+year , new CloudStoreUtil.EventModelsCallback() {
            @Override
            public void onSuccess(ArrayList<EventModel> myItems) {
                timeTableAdapter = new EmployeeTimeTableAdapter(getContext(), myItems.stream().map(eventModel -> eventModel.getFromTime() + "-" + eventModel.getToTime()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
                timeTableListView.setAdapter(timeTableAdapter);
            }
            @Override
            public void onFailure(Exception e) {
                timeTableAdapter = new EmployeeTimeTableAdapter(getContext(), new ArrayList<>());
                timeTableListView.setAdapter(timeTableAdapter);
//                Toast.makeText(getContext(), "Error getting employee timetable", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getEmployee() {
        CloudStoreUtil.getEmployeeByEmail(selectedEmployee.getEmail(), new CloudStoreUtil.EmployeeCallback() {
            @Override
            public void onSuccess(Employee myItem) {
                employee = myItem;
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), "Error getting employee", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int[] extraxtHM(String hoursEntry){
        int endH, endM;
        String[] endHM = hoursEntry.split(":");
        if(endHM.length == 2){
            endH = Integer.parseInt(endHM[0].trim());
            endM = Integer.parseInt(endHM[1].trim());
        }else{
            return new int[]{};
        }
        return new int[]{endH, endM};
    }
    private boolean isBefore(int firstHour, int firstMinute, int secondHour, int secondMinute, boolean equal){
        //firstHour:firstMinute is before secondHour:secondMinute
        //is before (or equal if equal = true)
        return firstHour < secondHour || (firstHour == secondHour && (firstMinute < secondMinute || (firstMinute == secondMinute && equal)));
    }

    private boolean isInside(int targetHour, int targetMinute, int firstHour, int firstMinute, int secondHour, int secondMinute){
        return isBefore(firstHour, firstMinute, targetHour, targetMinute, true) && isBefore(targetHour, targetMinute, secondHour, secondMinute, true);
    }
}
