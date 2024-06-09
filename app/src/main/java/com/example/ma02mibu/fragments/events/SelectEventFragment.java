package com.example.ma02mibu.fragments.events;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.EventListAdapter;
import com.example.ma02mibu.adapters.EventListInfoAdapter;
import com.example.ma02mibu.model.Event;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class SelectEventFragment extends Fragment {

    private ListView eventListView;
    private EventListInfoAdapter adapter;
    private ArrayList<Event> events;
    private FirebaseAuth auth;
    public SelectEventFragment() { }
    public static SelectEventFragment newInstance() {
        SelectEventFragment fragment = new SelectEventFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_event, container, false);
        eventListView = view.findViewById(R.id.selectEventList);
        getEvents();
        eventListView.setOnItemClickListener((parent, view1, position, id) -> {
            FragmentTransition.to(ExploreAndFilter.newInstance(adapter.getItem(position)), getActivity(), true, R.id.products_container, "productsManagement");
        });

        return view;
    }

    private void getEvents() {
        CloudStoreUtil.selectEventsFrom(auth.getCurrentUser().getEmail(), result -> {
            events = result;
            events.add(new Event(1L,"y", "turica", "x", 3, "x", true, "x", "test"));
            adapter = new EventListInfoAdapter(getActivity(), result);
            eventListView.setAdapter(adapter);
        });
    }
}