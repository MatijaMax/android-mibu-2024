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
import com.example.ma02mibu.databinding.FragmentSignUpPUP4Binding;
import com.example.ma02mibu.model.Category;
import com.example.ma02mibu.model.EventOrganizer;
import com.example.ma02mibu.model.EventType;
import com.example.ma02mibu.model.Owner;
import com.example.ma02mibu.model.OwnerRequest;
import com.example.ma02mibu.model.UserRole;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.model.mutation.ArrayTransformOperation;

import java.util.ArrayList;

public class SignUpPUP4Fragment extends Fragment {

    private ArrayList<Category> categories;
    private ArrayList<EventType> eventTypes;
    private ArrayList<String> selectedCategories = new ArrayList<>();
    private ArrayList<String> selectedEventTypes = new ArrayList<>();
    private CategoryListAdapter categoryListAdapter;
    private EventTypeListAdapter eventTypeListAdapter;
    private ListView categoryListView;
    private ListView eventTypeListView;


    private FirebaseAuth auth;

    private static final String ARG_PARAM1 = "owner";
    private static final String ARG_PARAM2 = "password";
    private Owner owner;
    private String password;

    public static SignUpPUP4Fragment newInstance(Owner owner, String password) {
        SignUpPUP4Fragment fragment = new SignUpPUP4Fragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, owner);
        args.putString(ARG_PARAM2, password);
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
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSignUpPUP4Binding binding = FragmentSignUpPUP4Binding.inflate(inflater, container, false);

        categoryListView = binding.categoryListView;
        eventTypeListView = binding.eventTypeListView;

        auth = FirebaseAuth.getInstance();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getCategories();
        getEventTypes();

        categoryListView.setOnItemClickListener((parent, view1, position, id) -> {
            ((CategoryListAdapter)categoryListView.getAdapter()).toggleCheckedWithView(position, view, parent);
            Category temp = ((CategoryListAdapter)categoryListView.getAdapter()).getItem(position);
            Log.d(TAG, "onViewCreated: OVDE SUUUUUUUUUUUU");
            if(selectedCategories.contains(temp.getDocumentRefId())){
                selectedCategories.remove(temp.getDocumentRefId());
            }else{
                selectedCategories.add(temp.getDocumentRefId());
            }
            String s = "";
            for(String c: selectedCategories){
                s += c;
            }
            Log.d(TAG, "onViewCreated: OVDE SUUUUUUUUUUUU" + s);
        });

        eventTypeListView.setOnItemClickListener((parent, view1, position, id) -> {
            ((EventTypeListAdapter)eventTypeListView.getAdapter()).toggleCheckedWithView(position, view1, parent);
            EventType temp = ((EventTypeListAdapter)eventTypeListView.getAdapter()).getItem(position);
            if(selectedEventTypes.contains(temp.getDocumentRefId())){
                selectedEventTypes.remove(temp.getDocumentRefId());
            }else{
                selectedEventTypes.add(temp.getDocumentRefId());
            }
            Log.d(TAG, "onViewCreated: OVDE SUUUUUUUUUUUU");
            String s = "";
            for(String c: selectedEventTypes){
                s += c;
            }
            Log.d(TAG, "onViewCreated: OVDE SUUUUUUUUUUUU" + s);
        });

        Button btnLogin = view.findViewById(R.id.btnSignUpPUP);
        btnLogin.setOnClickListener(v -> {
            if(selectedCategories.isEmpty()){
                return;
            }
            createAccount();
        });

    }

    private void createAccount() {
        Log.d(TAG, "createAccount:" + owner.getEmail());

        CloudStoreUtil.insertOwnerRequest(new OwnerRequest(owner, password));

        //TODO remove this down
        auth.createUserWithEmailAndPassword(owner.getEmail(), password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        if(user != null){
                            owner.setUserUID(user.getUid());
                            owner.setEventTypes(selectedEventTypes);
                            owner.setCategories(selectedCategories);
                            createOwner(owner);
                            createUserRole(new UserRole(user.getEmail(), UserRole.USERROLE.OWNER));
                            sendEmailVerification(user);
                        }

                    } else {
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

    @Override
    public void onResume() {
        super.onResume();
        getCategories();
        getEventTypes();
    }

    private void getCategories(){
        CloudStoreUtil.selectCategories(result -> {
            categories = result;
            categoryListAdapter = new CategoryListAdapter(getActivity(), categories);
            categoryListView.setAdapter(categoryListAdapter);
        });
    }

    private void getEventTypes(){
        CloudStoreUtil.selectEventTypes(result -> {
            eventTypes = result;
            eventTypeListAdapter = new EventTypeListAdapter(getActivity(), eventTypes);
            eventTypeListView.setAdapter(eventTypeListAdapter);
        });
    }

}