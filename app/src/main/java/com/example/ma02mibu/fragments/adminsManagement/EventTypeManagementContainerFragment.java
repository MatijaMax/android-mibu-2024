package com.example.ma02mibu.fragments.adminsManagement;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;

public class EventTypeManagementContainerFragment extends Fragment {

    public EventTypeManagementContainerFragment() { }

    public static EventTypeManagementContainerFragment newInstance(String param1, String param2) {
        EventTypeManagementContainerFragment fragment = new EventTypeManagementContainerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentTransition.to(EventTypeManagementTabFragment.newInstance(),
                getActivity(),
                true,
                R.id.eventTypeManagementContainer,
                "eventTypeManagement");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_type_management_container, container, false);
    }
}