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
import androidx.fragment.app.FragmentActivity;

import com.example.ma02mibu.R;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.EmployeeInService;

import java.util.ArrayList;

public class EmployeePickupListAdapter extends ArrayAdapter<EmployeeInService> {
    private final ArrayList<EmployeeInService> aEmployees;
    private FragmentActivity currFragActivity;
    public EmployeePickupListAdapter(Context context, ArrayList<EmployeeInService> employees, FragmentActivity fragmentActivity){
        super(context, R.layout.fragment_employee_card, employees);
        aEmployees = employees;
        currFragActivity = fragmentActivity;
    }
    @Override
    public int getCount() {
        return aEmployees.size();
    }

    @Nullable
    @Override
    public EmployeeInService getItem(int position) {
        return aEmployees.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        EmployeeInService employee = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_employee_card,
                    parent, false);
        }
        ImageView imageView = convertView.findViewById(R.id.employee_image);
        TextView employeeFirstName = convertView.findViewById(R.id.employee_first_name);
        TextView employeeLastName = convertView.findViewById(R.id.employee_last_name);
        TextView employeeEmail = convertView.findViewById(R.id.employee_email);
        convertView.findViewById(R.id.button_employee_details).setVisibility(View.GONE);
        convertView.findViewById(R.id.button_employee_calendar).setVisibility(View.GONE);
        convertView.findViewById(R.id.button_employee_enable).setVisibility(View.GONE);
        if(employee != null){
            imageView.setImageResource(R.drawable.employee_avatar);
            employeeFirstName.setText(employee.getFirstName());
            employeeLastName.setText(employee.getLastName());
            employeeEmail.setText(employee.getEmail());
        }
        return convertView;
    }
}
