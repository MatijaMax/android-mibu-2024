package com.example.ma02mibu.fragments.events;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;

import com.example.ma02mibu.activities.AuthenticationActivity;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.activities.MainActivity;
import com.example.ma02mibu.databinding.FragmentEventPageBinding;
import com.example.ma02mibu.databinding.FragmentUserProfileBinding;
import com.example.ma02mibu.fragments.employees.EmployeeListFragment;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.EventOrganizer;
import com.example.ma02mibu.model.Owner;
import com.example.ma02mibu.model.UserRole;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class UserProfileFragment extends Fragment {

    private FragmentUserProfileBinding binding;

    private Boolean isOrganizer = false;

    private Boolean isOwner = false;

    private Boolean isEmployee = false;

    private EventOrganizer mOrganizer;
    private Owner mOwner;

    private Employee mEmployee;


    private FirebaseAuth auth;

    private FirebaseUser currentUser;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            auth = FirebaseAuth.getInstance();
            currentUser = auth.getCurrentUser();


    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        loadUser(root.getRootView());

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        TextView tvCurrentFirstName = view.findViewById(R.id.tv_current_first_name);
        EditText etFirstName = view.findViewById(R.id.et_first_name);
        TextView tvCurrentEmail = view.findViewById(R.id.tv_current_email);
        EditText etCurrentPassword = view.findViewById(R.id.et_current_password);
        EditText etNewPassword = view.findViewById(R.id.et_new_password);
        EditText etConfirmNewPassword = view.findViewById(R.id.et_confirm_new_password);

        Button btnSave = view.findViewById(R.id.btn_save_changes);
        Button btnUpdateInfo = view.findViewById(R.id.btn_save_info);
        tvCurrentEmail.setText(currentUser.getEmail());

        if(isEmployee ){
            tvCurrentFirstName.setText(mEmployee.getFirstName()+" " + mEmployee.getLastName());
            tvCurrentEmail.setText(currentUser.getEmail());

        }
        if(isOrganizer ){
            tvCurrentFirstName.setText(mOrganizer.getName());
            tvCurrentEmail.setText(currentUser.getEmail());

        }
        if(isOwner){
            tvCurrentFirstName.setText(mOwner.getName());
            tvCurrentEmail.setText(currentUser.getEmail());

        }
        /*if(currentOwner != null){
            tvCompanyName.setText(currentOwner.getMyCompany().getName());
            tvCompanyEmail.setText(currentOwner.getMyCompany().getEmail());
            tvCompanyAddress.setText(currentOwner.getMyCompany().getAddress());
            tvCompanyPhone.setText(currentOwner.getMyCompany().getPhoneNumber());
            tvCompanyDescription.setText(currentOwner.getMyCompany().getDescription());
            tvWorkSchedule.setText("Work Schedule: 9 AM - 5 PM");
        } */



        btnUpdateInfo.setOnClickListener(v -> {





        });

        btnSave.setOnClickListener(v -> {
            String currentPassword = etCurrentPassword.getText().toString().trim();
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmNewPassword = etConfirmNewPassword.getText().toString().trim();

            if (validateInputs(currentPassword, newPassword, confirmNewPassword, view)) {
                updatePassword(currentPassword, newPassword);
            }
        });

    }


    private boolean validateInputs(String currentPassword, String newPassword, String confirmNewPassword,@NonNull View view ) {
        EditText etCurrentPassword = view.findViewById(R.id.et_current_password);
        EditText etNewPassword = view.findViewById(R.id.et_new_password);
        EditText etConfirmNewPassword = view.findViewById(R.id.et_confirm_new_password);
        if (TextUtils.isEmpty(currentPassword)) {
            etCurrentPassword.setError("Current password is required");
            return false;
        }

        if (TextUtils.isEmpty(newPassword)) {
            etNewPassword.setError("New password is required");
            return false;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            etConfirmNewPassword.setError("Passwords do not match");
            return false;
        }



        return true;
    }

    private void updatePassword(String currentPassword, String newPassword) {


        if (currentUser != null && currentUser.getEmail() != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), currentPassword);

            currentUser.reauthenticate(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    currentUser.updatePassword(newPassword).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Toast.makeText(getActivity(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Failed to update password: " + task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Reauthentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void loadUser(@NonNull View view){
        TextView tvCurrentFirstName = view.findViewById(R.id.tv_current_first_name);
        CloudStoreUtil.selectUserRoleFor(currentUser.getEmail(), result -> {
            if(result == null) {
                return;
            }

            if(result.getUserRole() == UserRole.USERROLE.ORGANIZER){

                CloudStoreUtil.getEventOrganizer(currentUser.getUid(), new CloudStoreUtil.EventOrganizerCallback() {
                    @Override
                    public void onSuccess(EventOrganizer myItem) {
                        mOrganizer = myItem;
                        isOrganizer = true;
                        Log.e("FIREBASE ORGANIZER", String.valueOf(isOrganizer));
                        tvCurrentFirstName.setText(mOrganizer.getName());

                    }
                    @Override
                    public void onFailure(Exception e) {
                        System.err.println("Error fetching owner document: " + e.getMessage());
                    }
                });


            } else if(result.getUserRole() == UserRole.USERROLE.OWNER){


                CloudStoreUtil.getOwner(currentUser.getUid(), new CloudStoreUtil.OwnerCallback() {
                    @Override
                    public void onSuccess(Owner myItem) {
                        mOwner = myItem;
                        isOwner = true;
                        Log.e("FIREBASE OWNER", String.valueOf(isOwner));
                        tvCurrentFirstName.setText(mOwner.getName());

                    }
                    @Override
                    public void onFailure(Exception e) {
                        System.err.println("Error fetching owner document: " + e.getMessage());
                    }
                });

            } else if(result.getUserRole() == UserRole.USERROLE.EMPLOYEE){


                CloudStoreUtil.getEmployee(currentUser.getUid(), new CloudStoreUtil.EmployeeCallback() {
                    @Override
                    public void onSuccess(Employee myItem) {
                        mEmployee = myItem;
                        isEmployee = true;
                        Log.e("FIREBASE EMPLOYEE", String.valueOf(isEmployee));
                        tvCurrentFirstName.setText(mEmployee.getFirstName()+" " + mEmployee.getLastName());

                    }
                    @Override
                    public void onFailure(Exception e) {
                        System.err.println("Error fetching owner document: " + e.getMessage());
                    }
                });

            }

            });

    }



        @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}

