package com.example.ma02mibu.fragments.authentication;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.AuthenticationActivity;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.activities.MainActivity;
import com.example.ma02mibu.activities.SplashScreenActivity;
import com.example.ma02mibu.databinding.FragmentLoginBinding;
import com.example.ma02mibu.fragments.employees.EmployeePersonalWorkCalendarFragment;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.WorkSchedule;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class LoginFragment extends Fragment {

    private FirebaseAuth auth;
    private FragmentLoginBinding binding;

    private EditText email;
    private EditText password;

    public LoginFragment() { }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        email = binding.email;
        password = binding.password;

        auth = FirebaseAuth.getInstance();
//
//        if(auth.getCurrentUser() != null){
//            Intent intent = new Intent(getActivity(), MainActivity.class);
//            startActivity(intent);
//        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button btnLogin = view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
            if(!inputIsValid()){
                return;
            }

            signIn();

//            auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(signInTask -> {
//                if(signInTask.isSuccessful()){
//                    Toast.makeText(view.getContext(), "Succesfully logged in!", Toast.LENGTH_SHORT).show();
//
//                    Intent intent = new Intent(getActivity(), MainActivity.class);
//                    startActivity(intent);
//                }
//                else{
//                    Toast.makeText(view.getContext(), "Wrong username or password!", Toast.LENGTH_SHORT).show();
//                }
//            });
        });
    }

    private boolean inputIsValid() {
        boolean valid = true;
        if(email.getText().toString().isEmpty()){
            email.setError("Email is required");
            valid = false;
        }
        if(password.getText().toString().isEmpty()){
            password.setError("Password is required");
            valid = false;
        }
        return valid;
    }


    private void signIn() {
        Log.d(TAG, "signIn:" + email.getText().toString());

        auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = auth.getCurrentUser();
                        if(user.isEmailVerified()){
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
//                            CloudStoreUtil.getEmployee(user.getUid(), new CloudStoreUtil.EmployeeCallback() {
//                                @Override
//                                public void onSuccess(Employee myItem) {
//                                    System.out.println("Retrieved item: " + myItem);
//                                    if(myItem.getIsActive() == 1){
//                                        Toast.makeText(getContext(), "You have been blocked!",
//                                                Toast.LENGTH_LONG).show();
//                                        auth.signOut();
//                                    }else {
//                                        Intent intent = new Intent(getActivity(), MainActivity.class);
//                                        startActivity(intent);
//                                    }
//                                }
//                                @Override
//                                public void onFailure(Exception e) {
//                                    Intent intent = new Intent(getActivity(), MainActivity.class);
//                                    startActivity(intent);
//                                }
//                            });
                        }
                        else{
                            Toast.makeText(getContext(), "Please verify your email",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(getContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}