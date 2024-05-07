package com.example.ma02mibu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.fragments.employees.EmployeeDetailsFragment;
import com.example.ma02mibu.fragments.employees.EmployeeWorkCalendarFragment;
import com.example.ma02mibu.model.Employee;

import java.util.ArrayList;

public class EmployeeListAdapter extends ArrayAdapter<Employee>{
    private final ArrayList<Employee> aEmployees;
    private final String ownerRefId;
    private FragmentActivity currFragActivity;
    public EmployeeListAdapter(Context context, ArrayList<Employee> employees, String ownerRefId, FragmentActivity fragmentActivity){
        super(context, R.layout.fragment_employee_card, employees);
        aEmployees = employees;
        this.ownerRefId = ownerRefId;
        currFragActivity = fragmentActivity;
    }
    @Override
    public int getCount() {
        return aEmployees.size();
    }

    @Nullable
    @Override
    public Employee getItem(int position) {
        return aEmployees.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Employee employee = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_employee_card,
                    parent, false);
        }
        ImageView imageView = convertView.findViewById(R.id.employee_image);
        TextView employeeFirstName = convertView.findViewById(R.id.employee_first_name);
        TextView employeeLastName = convertView.findViewById(R.id.employee_last_name);
        TextView employeeEmail = convertView.findViewById(R.id.employee_email);
        Button detailsButton = convertView.findViewById(R.id.button_employee_details);
        handleDetailsButtonClick(detailsButton, imageView, employee);
        Button workCalendarButton = convertView.findViewById(R.id.button_employee_calendar);
        handleWorkCalendarButtonClick(workCalendarButton, imageView, employee);
        Button enableButton = convertView.findViewById(R.id.button_employee_enable);
        if(employee != null){
            imageView.setImageResource(employee.getImage());
            employeeFirstName.setText(employee.getFirstName());
            employeeLastName.setText(employee.getLastName());
            employeeEmail.setText(employee.getEmail());
            enableButton.setText(employee.getIsActive()==0 ? "disable" :"enable");
        }
        return convertView;
    }

    private void handleDetailsButtonClick(Button detailsButton, ImageView imageView, Employee employee){
        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(EmployeeDetailsFragment.newInstance(employee, ownerRefId), currFragActivity,
                        true, R.id.scroll_employees_list, "EmployeeDetailsPage");
            }
        });
    }

    private void handleWorkCalendarButtonClick(Button detailsButton, ImageView imageView, Employee employee){
        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(EmployeeWorkCalendarFragment.newInstance(employee, ""), currFragActivity,
                        true, R.id.scroll_employees_list, "EmployeeWorkCalendarPage");
            }
        });
    }
}
