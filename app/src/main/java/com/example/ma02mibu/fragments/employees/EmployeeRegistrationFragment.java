package com.example.ma02mibu.fragments.employees;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.MainActivity;
import com.example.ma02mibu.adapters.EmployeeListAdapter;
import com.example.ma02mibu.databinding.FragmentEmployeeRegistrationBinding;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.fragments.products.ProductsListFragment;
import com.example.ma02mibu.model.Company;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.Owner;
import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.model.UserRole;
import com.example.ma02mibu.model.WorkSchedule;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class EmployeeRegistrationFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private String ownerRefId;
    private Owner currentOwner;
    private FirebaseUser cuurentUser;
    private FirebaseAuth auth;

    public EmployeeRegistrationFragment() {
        // Required empty public constructor
    }

    public static EmployeeRegistrationFragment newInstance(String param1) {
        EmployeeRegistrationFragment fragment = new EmployeeRegistrationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ownerRefId = getArguments().getString(ARG_PARAM1);
        }
    }

    private FragmentEmployeeRegistrationBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEmployeeRegistrationBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        auth = FirebaseAuth.getInstance();
        cuurentUser = auth.getCurrentUser();
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
        loadCompany();
        return view;
    }

    private void addEmployee() throws InterruptedException {
        String fName = binding.etFirstName.getText().toString();
        String lName = binding.etLastName.getText().toString();
        String email = binding.etEmail.getText().toString();
        String pass1 = binding.etPassword.getText().toString();
        String pass2 = binding.etPasswordAgain.getText().toString();
        if (!pass1.equals(pass2)) {
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
        if (fName.isEmpty() || lName.isEmpty() || email.isEmpty() || pass1.isEmpty() || address.isEmpty() || phoneNr.isEmpty()) {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Fields empty");
            alertDialog.setMessage("Fields can not be left empty!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // No action needed here since the button does nothing.
                }
            });
            alertDialog.show();
            return;
        }
        Employee e = new Employee(1L, fName, lName, email, pass1, address, phoneNr, R.drawable.employee_avatar, ownerRefId,  0);

        String mondayHours = binding.etMondayHours.getText().toString();
        String tuesdayHours = binding.etTuesdayHours.getText().toString();
        String wedHours = binding.etWednesdayHours.getText().toString();
        String thurHours = binding.etThursdayHours.getText().toString();
        String friHours = binding.etFridayHours.getText().toString();
        String satHours = binding.etSaturdayHours.getText().toString();
        String sunHours = binding.etSundayHours.getText().toString();
        if(mondayHours.isEmpty() && tuesdayHours.isEmpty() && wedHours.isEmpty() && thurHours.isEmpty() && friHours.isEmpty()
        && satHours.isEmpty() && sunHours.isEmpty()){
            e.setSchedule(currentOwner.getMyCompany().getWorkSchedule());
        }else{
            WorkSchedule customWorkSchedule = new WorkSchedule();
            if(!mondayHours.isEmpty()){
                String[] dayHM = extraxtHM(mondayHours);
                if(dayHM.length == 4){
                    if(!checkValid(dayHM)){
                        return;
                    }
                    LocalTime t1 = LocalTime.of(Integer.parseInt(dayHM[0]), Integer.parseInt(dayHM[1]));
                    LocalTime t2 = LocalTime.of(Integer.parseInt(dayHM[2]), Integer.parseInt(dayHM[3]));
                    if(t1.isAfter(t2)){
                        Toast.makeText(getContext(), "Monday working hours are wrong.", Toast.LENGTH_LONG).show();
                        return;
                    }
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
                    if(!checkValid(dayHM)){
                        return;
                    }
                    LocalTime t1 = LocalTime.of(Integer.parseInt(dayHM[0]), Integer.parseInt(dayHM[1]));
                    LocalTime t2 = LocalTime.of(Integer.parseInt(dayHM[2]), Integer.parseInt(dayHM[3]));
                    if(t1.isAfter(t2)){
                        Toast.makeText(getContext(), "Tuesday working hours are wrong.", Toast.LENGTH_LONG).show();
                        return;
                    }
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
                    if(!checkValid(dayHM)){
                        return;
                    }
                    LocalTime t1 = LocalTime.of(Integer.parseInt(dayHM[0]), Integer.parseInt(dayHM[1]));
                    LocalTime t2 = LocalTime.of(Integer.parseInt(dayHM[2]), Integer.parseInt(dayHM[3]));
                    if(t1.isAfter(t2)){
                        Toast.makeText(getContext(), "Wednesday working hours are wrong.", Toast.LENGTH_LONG).show();
                        return;
                    }
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
                    if(!checkValid(dayHM)){
                        return;
                    }
                    LocalTime t1 = LocalTime.of(Integer.parseInt(dayHM[0]), Integer.parseInt(dayHM[1]));
                    LocalTime t2 = LocalTime.of(Integer.parseInt(dayHM[2]), Integer.parseInt(dayHM[3]));
                    if(t1.isAfter(t2)){
                        Toast.makeText(getContext(), "Thursday working hours are wrong.", Toast.LENGTH_LONG).show();
                        return;
                    }
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
                    if(!checkValid(dayHM)){
                        return;
                    }
                    LocalTime t1 = LocalTime.of(Integer.parseInt(dayHM[0]), Integer.parseInt(dayHM[1]));
                    LocalTime t2 = LocalTime.of(Integer.parseInt(dayHM[2]), Integer.parseInt(dayHM[3]));
                    if(t1.isAfter(t2)){
                        Toast.makeText(getContext(), "Friday working hours are wrong.", Toast.LENGTH_LONG).show();
                        return;
                    }
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
                    if(!checkValid(dayHM)){
                        return;
                    }
                    LocalTime t1 = LocalTime.of(Integer.parseInt(dayHM[0]), Integer.parseInt(dayHM[1]));
                    LocalTime t2 = LocalTime.of(Integer.parseInt(dayHM[2]), Integer.parseInt(dayHM[3]));
                    if(t1.isAfter(t2)){
                        Toast.makeText(getContext(), "Saturday working hours are wrong.", Toast.LENGTH_LONG).show();
                        return;
                    }
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
                    if(!checkValid(dayHM)){
                        return;
                    }
                    LocalTime t1 = LocalTime.of(Integer.parseInt(dayHM[0]), Integer.parseInt(dayHM[1]));
                    LocalTime t2 = LocalTime.of(Integer.parseInt(dayHM[2]), Integer.parseInt(dayHM[3]));
                    if(t1.isAfter(t2)){
                        Toast.makeText(getContext(), "Sunday working hours are wrong.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    customWorkSchedule.setWorkTime(DayOfWeek.SUNDAY, LocalTime.of(Integer.parseInt(dayHM[0]), Integer.parseInt(dayHM[1])), LocalTime.of(Integer.parseInt(dayHM[2]), Integer.parseInt(dayHM[3])));
                }else{
                    alertShow("Sunday");
                    return;
                }
            }else{
                customWorkSchedule.setWorkTime(DayOfWeek.SUNDAY, null, null);
            }
            customWorkSchedule.setStartDay(null);
            customWorkSchedule.setEndDay(null);
            e.setSchedule(customWorkSchedule);
        }

        //cuvam u bazu zaposlenog i prebacujem na listu svih
//        auth.signOut();
        createAccount(e);
        //Thread.sleep(600);

    }

    private boolean checkValid(String[] dayHM){
        for (int i = 0; i < 4; i ++){
            if (dayHM[i].length() != 2) {
                Toast.makeText(getContext(), "Wrong time format entry.", Toast.LENGTH_LONG).show();
                return false;
            }
            Integer r = parseIntOrNull(dayHM[i]);
            if(r == null){
                Toast.makeText(getContext(), "Wrong time entry.", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        int hours1 = Integer.parseInt(dayHM[0]);
        int minutes1 = Integer.parseInt(dayHM[1]);
        if(hours1 < 0 || hours1 > 23){
            Toast.makeText(getContext(), "Wrong time hours entry.", Toast.LENGTH_LONG).show();
            return false;
        }
        if(minutes1 < 0 || minutes1 > 59){
            Toast.makeText(getContext(), "Wrong time minutes entry.", Toast.LENGTH_LONG).show();
            return false;
        }
        hours1 = Integer.parseInt(dayHM[2]);
        minutes1 = Integer.parseInt(dayHM[3]);
        if(hours1 < 0 || hours1 > 23){
            Toast.makeText(getContext(), "Wrong time hours entry.", Toast.LENGTH_LONG).show();
            return false;
        }
        if(minutes1 < 0 || minutes1 > 59){
            Toast.makeText(getContext(), "Wrong time minutes entry.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public Integer parseIntOrNull(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void updateUser() {
        auth.updateCurrentUser(cuurentUser).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i("GGREs",cuurentUser.getUid()+cuurentUser.getEmail());
                FirebaseUser u = auth.getCurrentUser();
                Log.i("HHHHHHHHHHHHHHHHHH", u.getEmail());
                FragmentTransition.to(EmployeeListFragment.newInstance(), getActivity(), true, R.id.scroll_employees_list, "EmployeeRegistration");
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
                            employee.setUserUID(user.getUid());
                            insert(employee);
                            UserRole ur = new UserRole(user.getEmail(), UserRole.USERROLE.EMPLOYEE);
                            CloudStoreUtil.insertUserRole(ur);
                            sendEmailVerification(user);
                        }

                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(getContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void insert(Employee employee){
        CloudStoreUtil.insertEmployeeNew(employee);
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
    private void loadCompany() {
        CloudStoreUtil.getOwner(ownerRefId, new CloudStoreUtil.OwnerCallback() {
            @Override
            public void onSuccess(Owner myItem) {
                // Handle the retrieved item (e.g., display it in UI)
                System.out.println("Retrieved item: " + myItem);
                currentOwner = myItem;
            }
            @Override
            public void onFailure(Exception e) {
                // Handle the failure (e.g., show an error message)
                System.err.println("Error fetching document: " + e.getMessage());
            }
        });
    }

    private void chooseImage(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivity(i);
    }
}