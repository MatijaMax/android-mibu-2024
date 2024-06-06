package com.example.ma02mibu.fragments.events;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.EmployeeListAdapter;
import com.example.ma02mibu.adapters.MyEventListAdapter;
import com.example.ma02mibu.databinding.FragmentEmployeeListBinding;
import com.example.ma02mibu.databinding.FragmentEventListBinding;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.Event;
import com.example.ma02mibu.model.Guest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class MyEventListFragment extends ListFragment {
    private FirebaseAuth auth;
    private FragmentEventListBinding binding;
    private ArrayList<Event> mEvents;
    private ArrayList<Event> mEventsBackup;
    private MyEventListAdapter adapter;

    private String ownerRefId;
    private String email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            ownerRefId = user.getUid();
            email = user.getEmail();
        }

        binding = FragmentEventListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*
        Button newEmployeeButton = binding.btnAddNewEmployee;
        newEmployeeButton.setOnClickListener(v -> {
            FragmentTransition.to(EmployeeRegistrationFragment.newInstance(ownerRefId), getActivity(),
                    true, R.id.scroll_employees_list, "newEmployeePage");
        });
        */

        Log.i("MY EVENT LIST", ownerRefId);
        loadEvents();
        return root;
    }

    private void loadEvents() {
        CloudStoreUtil.getEvents(email, new CloudStoreUtil.EventsCallback() {
            @Override
            public void onSuccess(ArrayList<Event> itemList) {
                // Handle the retrieved list of items (e.g., display them in UI)
                mEvents = new ArrayList<>(itemList);
                mEventsBackup = new ArrayList<>(itemList);
                adapter = new MyEventListAdapter(getActivity(), mEvents, email, getActivity());
                setListAdapter(adapter);
                for (Event e: mEvents){
                    if (e.getGuests() != null) {
                        Log.d("FIREBASE MONKEY", String.valueOf(e.getGuests().size()));
                    } else {
                        Log.d("FIREBASE MONKEY", "Guests list is null");
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                // Handle the failure (e.g., show an error message)
                System.err.println("Error fetching documents: " + e.getMessage());
            }
        });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private MyEventListFragment(){}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        //mEmployees.clear();
    }


    public static MyEventListFragment newInstance(){
        MyEventListFragment fragment = new MyEventListFragment();
//        Bundle args = new Bundle();
//        args.putParcelableArrayList(ARG_EMPLOYEES, employees);
//        fragment.setArguments(args);
        return fragment;
    }


}
