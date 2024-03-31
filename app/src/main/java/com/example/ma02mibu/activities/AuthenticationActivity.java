package com.example.ma02mibu.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.fragments.authentication.LoginFragment;
import com.example.ma02mibu.fragments.authentication.SignUpODFragment;
import com.example.ma02mibu.fragments.authentication.SignUpPUPFragment;

public class AuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_authentication);

        Button btnSignUpPUP = findViewById(R.id.btn_signUpPUP);
        btnSignUpPUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(SignUpPUPFragment.newInstance(), AuthenticationActivity.this, false, R.id.authenticationFragmentContainer);
            }
        });

        Button btnSignUpOD = findViewById(R.id.btn_signUpOD);
        btnSignUpOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(SignUpODFragment.newInstance(), AuthenticationActivity.this, false, R.id.authenticationFragmentContainer);
            }
        });

        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(LoginFragment.newInstance(), AuthenticationActivity.this, false, R.id.authenticationFragmentContainer);
            }
        });
    }
}