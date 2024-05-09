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
import android.widget.ListView;
import android.widget.Toast;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.activities.MainActivity;
import com.example.ma02mibu.adapters.authentication.CategoryListAdapter;
import com.example.ma02mibu.adapters.authentication.EventTypeListAdapter;
import com.example.ma02mibu.model.Category;
import com.example.ma02mibu.model.EventOrganizer;
import com.example.ma02mibu.model.EventType;
import com.example.ma02mibu.model.Owner;
import com.example.ma02mibu.model.UserRole;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class SignUpPUP4Fragment extends Fragment {

    //private ArrayList<Category> mCategories;
    //private CategoryListAdapter mCategoriesAdapter;
    //private ArrayList<EventType> mEventTypes;
    //private EventTypeListAdapter mEventTypesAdapter;

    private static final String ARG_PARAM3 = "param1";
    private static final String ARG_PARAM4 = "param2";


    private FirebaseAuth auth;

    private static final String ARG_PARAM1 = "owner";
    private static final String ARG_PARAM2 = "password";
    private Owner owner;
    private String password;

    public static SignUpPUP4Fragment newInstance(Owner owner, String password, ArrayList<Category> categories, ArrayList<EventType> eventTypes) {
        SignUpPUP4Fragment fragment = new SignUpPUP4Fragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, owner);
        args.putString(ARG_PARAM2, password);
        args.putParcelableArrayList(ARG_PARAM3, categories);
        args.putParcelableArrayList(ARG_PARAM4, eventTypes);
        fragment.setArguments(args);
        return fragment;
    }

    public SignUpPUP4Fragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            owner = getArguments().getParcelable(ARG_PARAM1);
            password = getArguments().getString(ARG_PARAM2);
            //mCategories = getArguments().getParcelableArrayList(ARG_PARAM2);
            //mCategoriesAdapter = new CategoryListAdapter(getActivity(), mCategories);
            //mEventTypes = getArguments().getParcelableArrayList(ARG_PARAM3);
            //mEventTypesAdapter = new EventTypeListAdapter(getActivity(), mEventTypes);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        auth = FirebaseAuth.getInstance();

        return inflater.inflate(R.layout.fragment_sign_up_p_u_p4, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ListView categoryListView = view.findViewById(R.id.categoryListView);
        ListView eventTypeListView = view.findViewById(R.id.eventTypeListView);
        //categoryListView.setAdapter(mCategoriesAdapter);
        //eventTypeListView.setAdapter(mEventTypesAdapter);

        Button btnLogin = view.findViewById(R.id.btnSignUpPUP);
        btnLogin.setOnClickListener(v -> {
            createAccount();

            FragmentTransition.to(LoginFragment.newInstance(), requireActivity(), false, R.id.authenticationFragmentContainer, "signUpPUP");
        });

    }

    private void createAccount() {
        Log.d(TAG, "createAccount:" + owner.getEmail());

        auth.createUserWithEmailAndPassword(owner.getEmail(), password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = auth.getCurrentUser();
                        sendEmailVerification(user);
                        owner.setUserUID(user.getUid());
                        createOwner(owner);
                        createUserRole(new UserRole(user.getEmail(), UserRole.USERROLE.OWNER));
                        singOut();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(getContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createUserRole(UserRole userRole) {
        CloudStoreUtil.insertUserRole(userRole);
    }

    private void createOwner(Owner owner) {
        CloudStoreUtil.insertOwner(owner);
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

}