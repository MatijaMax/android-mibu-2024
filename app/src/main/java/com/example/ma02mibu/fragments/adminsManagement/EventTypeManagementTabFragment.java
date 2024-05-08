package com.example.ma02mibu.fragments.adminsManagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.adminsManagment.EventTypeListAdapter;
import com.example.ma02mibu.databinding.FragmentEventTypeManagementTabBinding;
import com.example.ma02mibu.model.EventType;

import java.util.ArrayList;

public class EventTypeManagementTabFragment extends Fragment {

    private ListView eventTypeListView;
    private EventTypeListAdapter eventTypeListAdapter;
    private ArrayList<EventType> eventTypes = new ArrayList<>();
    public EventTypeManagementTabFragment() { }

    public static EventTypeManagementTabFragment newInstance() {
        EventTypeManagementTabFragment fragment = new EventTypeManagementTabFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventTypeListAdapter = new EventTypeListAdapter(getActivity(), eventTypes);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentEventTypeManagementTabBinding binding = FragmentEventTypeManagementTabBinding.inflate(inflater, container, false);

        eventTypeListView = binding.eventTypesListView;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getEventTypes();

        eventTypeListView.setOnItemClickListener((parent, view1, position, id) -> FragmentTransition.to(EventTypeEditFragment.newInstance(false, eventTypeListAdapter.getItem(position)),
                requireActivity(),
                true,
                R.id.eventTypeManagementContainer,
                "eventTypeEdit"));

        view.findViewById(R.id.addNewEventType).setOnClickListener(v -> FragmentTransition.to(EventTypeEditFragment.newInstance(true, new EventType()),
                requireActivity(),
                true,
                R.id.eventTypeManagementContainer,
                "eventTypeEdit"));
    }

    @Override
    public void onResume() {
        super.onResume();
        getEventTypes();
    }

    private void getEventTypes(){
        CloudStoreUtil.selectEventTypes(result -> {
            eventTypeListAdapter = new EventTypeListAdapter(getActivity(), result);
            eventTypeListView.setAdapter(eventTypeListAdapter);
        });
    }
}