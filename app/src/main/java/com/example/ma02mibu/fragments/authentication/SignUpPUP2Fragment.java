package com.example.ma02mibu.fragments.authentication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.databinding.FragmentSignUpPUP2Binding;
import com.example.ma02mibu.model.Company;
import com.example.ma02mibu.model.Owner;

import java.util.Objects;

public class SignUpPUP2Fragment extends Fragment {

    private static final String ARG_PARAM1 = "owner";
    private static final String ARG_PARAM2 = "password";

    private EditText email;
    private EditText name;
    private EditText address;
    private EditText phone;
    private EditText description;

    private Owner owner;
    private String password;

    public SignUpPUP2Fragment() {
    }

    public static SignUpPUP2Fragment newInstance(Owner owner, String password) {
        SignUpPUP2Fragment fragment = new SignUpPUP2Fragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, owner);
        args.putString(ARG_PARAM2, password);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            owner = getArguments().getParcelable(ARG_PARAM1);
            password = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        com.example.ma02mibu.databinding.FragmentSignUpPUP2Binding binding = FragmentSignUpPUP2Binding.inflate(inflater, container, false);

        email = binding.email;
        name = binding.name;
        phone = binding.phoneNumber;
        address = binding.address;
        description = binding.description;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button btnNext = view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(v -> {
            if(!fieldsAreValid()){
                return;
            }

            Company company = new Company(email.getText().toString(),
                                            name.getText().toString(),
                                            phone.getText().toString(),
                                            address.getText().toString(),
                                            description.getText().toString());
            owner.setMyCompany(company);
            FragmentTransition.to(SignUpPUP3Fragment.newInstance(owner, password), requireActivity(), true, R.id.authenticationFragmentContainer, "signUpPUP");
        });

        Button btnSelectImage = view.findViewById(R.id.btnSelectImage);
        btnSelectImage.setOnClickListener(v -> chooseImage());
    }

    private boolean fieldsAreValid(){
        boolean valid = true;
        if(email.getText().toString().isEmpty()){
            email.setError("Email is required");
            valid = false;
        }
        else{
            email.setError(null);
        }
        if(name.getText().toString().isEmpty()){
            name.setError("Name is required");
            valid = false;
        }
        else{
            name.setError(null);
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
        if(description.getText().toString().isEmpty()){
            description.setError("Surname is required");
            valid = false;
        }
        else{
            description.setError(null);
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