package com.example.ma02mibu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ma02mibu.R;
import com.example.ma02mibu.model.Employee;

import java.util.ArrayList;

public class EmployeeListAdapter extends ArrayAdapter<Employee>{
    private final ArrayList<Employee> aEmployees;
    public EmployeeListAdapter(Context context, ArrayList<Employee> employees){
        super(context, R.layout.fragment_employee_card, employees);
        aEmployees = employees;
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
        if(employee != null){
            imageView.setImageResource(employee.getImage());
            employeeFirstName.setText(employee.getFirstName());
            employeeLastName.setText(employee.getLastName());
            employeeEmail.setText(employee.getEmail());
        }

        return convertView;
    }
}
