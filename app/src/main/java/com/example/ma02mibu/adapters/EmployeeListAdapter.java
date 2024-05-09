package com.example.ma02mibu.adapters;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.fragments.employees.EmployeeDetailsFragment;
import com.example.ma02mibu.fragments.employees.EmployeeListFragment;
import com.example.ma02mibu.fragments.employees.EmployeeWorkCalendarFragment;
import com.example.ma02mibu.model.Employee;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class EmployeeListAdapter extends ArrayAdapter<Employee>{
    private final ArrayList<Employee> aEmployees;
    private final String ownerRefId;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FragmentActivity currFragActivity;
    public EmployeeListAdapter(Context context, ArrayList<Employee> employees, String ownerRefId, FragmentActivity fragmentActivity){
        super(context, R.layout.fragment_employee_card, employees);
        aEmployees = employees;
        this.ownerRefId = ownerRefId;
        currFragActivity = fragmentActivity;
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
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

    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(),
                                "Verification email sent to " + user.getEmail(),
                                Toast.LENGTH_SHORT).show();
                        updateUser();
                    } else {
                        Log.e(TAG, "sendEmailVerification", task.getException());
                        Toast.makeText(getContext(),
                                "Failed to send verification email.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUser() {
        auth.updateCurrentUser(currentUser).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i("GGREs",currentUser.getUid()+currentUser.getEmail());
                FirebaseUser u = auth.getCurrentUser();
                Log.i("HHHHHHHHHHHHHHHHHH", u.getEmail());
            } else {
                Log.i("GGREs","ASAS");
            }
        });
    }

    private void createAccount(Employee employee) {
        Log.d(TAG, "createAccount:" + employee.getEmail());

        auth.createUserWithEmailAndPassword(employee.getEmail(), employee.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        if(user != null){
                            employee.setIsActive(0);
                            employee.setUserUID(user.getUid());
                            Log.i("SASASSAASSAS", employee.getUserUID());
                            saveUpdate(employee);
                            sendEmailVerification(user);
                        }

                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());

                    }
                });
    }

    private void saveUpdate(Employee employee){
        CloudStoreUtil.updateEmployeesUid(employee, new CloudStoreUtil.UpdateUidCallback() {
            @Override
            public void onSuccess() {
                // Item updated successfully
                System.out.println("Item updated!");
            }
            @Override
            public void onFailure(Exception e) {
                // Handle the failure (e.g., show an error message)
                System.err.println("Error updating item: " + e.getMessage());
            }
        });
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
        handleEnableButtonClick(enableButton, employee);
        return convertView;
    }

    private void handleEnableButtonClick(Button enableButton, Employee employee){
        enableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(employee.getIsActive() == 0){
                    auth.signInWithEmailAndPassword(employee.getEmail(), employee.getPassword()).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser u = auth.getCurrentUser();
                            u.delete().addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful()) {
                                    updateUser();
                                    employee.setIsActive(1);
                                    saveUpdate(employee);
                                }else {
                                    Log.w(TAG, "deleteUser:failure", task.getException());
                                }
                            });
                        enableButton.setText("enable");
                        }else {
                            Log.w(TAG, "loginUserWithEmail:failure", task.getException());
                        }
                });}
                else{
                    createAccount(employee);
                    enableButton.setText("disable");
                }
            }
        });
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
