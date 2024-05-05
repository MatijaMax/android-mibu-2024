package com.example.ma02mibu.fragments.authentication;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.MainActivity;
import com.example.ma02mibu.databinding.FragmentSignUpODBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpODFragment extends Fragment {

    private FragmentSignUpODBinding binding;
    private ActionCodeSettings actionCodeSettings;
    private FirebaseAuth auth;

    private EditText email;
    private EditText password;
    private EditText repeatedPassword;
    private EditText name;
    private EditText surname;
    private EditText phone;
    private EditText address;


    public SignUpODFragment() { }

    public static SignUpODFragment newInstance() {
        SignUpODFragment fragment = new SignUpODFragment();
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
        binding = FragmentSignUpODBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();

        email = binding.email;
        password = binding.password;
        repeatedPassword = binding.repeatedPassword;
        name = binding.name;
        surname = binding.surname;
        phone = binding.phoneNumber;
        address = binding.address;

        actionCodeSettings =
                ActionCodeSettings.newBuilder()
                        // URL you want to redirect back to. The domain (www.example.com) for this
                        // URL must be whitelisted in the Firebase Console.
                        .setUrl("https://www.mibu.com/finishSignUp")
                        // This must be true
                        .setHandleCodeInApp(true)
                        .setIOSBundleId("com.example.ios")
                        .setAndroidPackageName(
                                "com.example.android",
                                true, /* installIfNotAvailable */
                                "12"    /* minimumVersion */)
                        .build();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button btnSignUp = view.findViewById(R.id.btnSignUpOD);
        btnSignUp.setOnClickListener(v -> {

            if(!fieldsAreValid()){
                return;
            }

            createAccount();

//            auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(createUserCommand -> {
//               if(createUserCommand.isSuccessful()){
//                   auth.sendSignInLinkToEmail(email.getText().toString(), actionCodeSettings)
//                           .addOnCompleteListener(sendMailTask -> {
//                               if (sendMailTask.isSuccessful()) {
//                                   Log.d(TAG, "Email sent.");
//                                   Toast.makeText(view.getContext(),"Account created successfully, check mail!",Toast.LENGTH_LONG).show();
//                               }
//                               else {
//                                   Log.d(TAG, sendMailTask.getResult().toString());
//                               }
//                           });
//               }
//               else {
//                   Toast.makeText(view.getContext(),createUserCommand.getException().getMessage(),Toast.LENGTH_LONG).show();
//               }
//            });
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

    private void createAccount() {
        Log.d(TAG, "createAccount:" + email.getText().toString());

        auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = auth.getCurrentUser();
                        sendEmailVerification(user);
                        singOut();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(getContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void singOut(){
        auth.signOut();
    }

    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(),
                                "Verification email sent to " + user.getEmail(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "sendEmailVerification", task.getException());
                        Toast.makeText(getContext(),
                                "Failed to send verification email.",
                                Toast.LENGTH_SHORT).show();
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