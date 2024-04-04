package com.example.ma02mibu.fragments.adminsManagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.adapters.adminsManagment.CategoryListAdapter;
import com.example.ma02mibu.adapters.adminsManagment.EventTypeListAdapter;
import com.example.ma02mibu.model.EventType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EventTypeManagementTabFragment extends Fragment {
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
        createEventTypes();
        eventTypeListAdapter = new EventTypeListAdapter(getActivity(), eventTypes);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_type_management_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ListView eventTypeListView = view.findViewById(R.id.eventTypesListView);
        eventTypeListView.setAdapter(eventTypeListAdapter);
        eventTypeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransition.to(EventTypeEditFragment.newInstance(false, eventTypeListAdapter.getItem(position)),
                        getActivity(),
                        true,
                        R.id.eventTypeManagementContainer,
                        "eventTypeEdit");
            }
        });

        ((FloatingActionButton) view.findViewById(R.id.addNewEventType)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(EventTypeEditFragment.newInstance(true, new EventType()),
                        getActivity(),
                        true,
                        R.id.eventTypeManagementContainer,
                        "eventTypeEdit");
            }
        });
    }

    private void createEventTypes(){
        eventTypes.add(new EventType(1L, "Event type name 1", "Event type description 1", EventType.EVENTTYPESTATUS.ACTIVE));
        eventTypes.add(new EventType(2L, "Event type name 2", "Event type description 2", EventType.EVENTTYPESTATUS.ACTIVE));
        eventTypes.add(new EventType(3L, "Event type name 3", "Event type description 3", EventType.EVENTTYPESTATUS.ACTIVE));
        eventTypes.add(new EventType(4L, "Event type name 4", "Event type description 4", EventType.EVENTTYPESTATUS.DEACTIVATED));
        eventTypes.add(new EventType(5L, "Event type name 5", "Event type description 5", EventType.EVENTTYPESTATUS.ACTIVE));
        eventTypes.add(new EventType(6L, "Event type name 6", "Event type description 6", EventType.EVENTTYPESTATUS.DEACTIVATED));
        eventTypes.add(new EventType(7L, "Event type name 7", "Event type description 7", EventType.EVENTTYPESTATUS.DEACTIVATED));
        eventTypes.add(new EventType(8L, "Event type name 8", "Event type description 8", EventType.EVENTTYPESTATUS.ACTIVE));
    }
}