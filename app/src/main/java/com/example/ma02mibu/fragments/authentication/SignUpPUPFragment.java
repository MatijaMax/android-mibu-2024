package com.example.ma02mibu.fragments.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.databinding.FragmentSignUpPUPBinding;
import com.example.ma02mibu.model.Owner;

public class SignUpPUPFragment extends Fragment {

    private EditText email;
    private EditText password;
    private EditText repeatedPassword;
    private EditText name;
    private EditText surname;
    private EditText phone;
    private EditText address;


    public SignUpPUPFragment() { }

    public static SignUpPUPFragment newInstance() {
        SignUpPUPFragment fragment = new SignUpPUPFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        com.example.ma02mibu.databinding.FragmentSignUpPUPBinding binding = FragmentSignUpPUPBinding.inflate(inflater, container, false);

        email = binding.email;
        password = binding.password;
        repeatedPassword = binding.repeatedPassword;
        name = binding.name;
        surname = binding.surname;
        phone = binding.phoneNumber;
        address = binding.address;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button btnNext = view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(v -> {
            if(!fieldsAreValid()){
                return;
            }
            Owner owner = new Owner(email.getText().toString(),
                                    name.getText().toString(),
                                    surname.getText().toString(),
                                    phone.getText().toString(),
                                    address.getText().toString(),
                                    "");
            FragmentTransition.to(SignUpPUP2Fragment.newInstance(owner, password.getText().toString()), requireActivity(), true, R.id.authenticationFragmentContainer, "signUpPUP");
        });

        Button btnSelectImage = view.findViewById(R.id.btnSelectImage);
        btnSelectImage.setOnClickListener(v -> chooseImage());
    }

    private boolean fieldsAreValid() {
        boolean valid = true;
        if(email.getText().toString().isEmpty()){
            email.setError("Email is required");
            valid = false;
        }
        else{
            email.setError(null);
        }
        if(password.getText().toString().isEmpty()){
            password.setError("Password is required");
            valid = false;
        }
        else{
            password.setError(null);
        }
        if(!repeatedPassword.getText().toString().equals(password.getText().toString())){
            repeatedPassword.setError("Passwords do not match");
            valid = false;
        }
        else{
            repeatedPassword.setError(null);
        }
        if(name.getText().toString().isEmpty()){
            name.setError("Name is required");
            valid = false;
        }
        else{
            name.setError(null);
        }
        if(surname.getText().toString().isEmpty()){
            surname.setError("Surname is required");
            valid = false;
        }
        else{
            surname.setError(null);
        }
        if(phone.getText().toString().isEmpty()){
            phone.setError("Phone number is required");
            valid = false;
        }
        else{
            phone.setError(null);
        }
        if(address.getText().toString().isEmpty()){
            address.setError("Address is required");
            valid = false;
        }
        else{
            address.setError(null);
        }
        return valid;
    }

    private void chooseImage(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivity(i);
    }
}