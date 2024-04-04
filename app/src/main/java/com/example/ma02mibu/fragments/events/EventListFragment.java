package com.example.ma02mibu.fragments.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.adapters.EventListAdapter;
import com.example.ma02mibu.databinding.EventListFragmentBinding;
import com.example.ma02mibu.model.Event;

import java.util.ArrayList;

public class EventListFragment extends ListFragment {

    private EventListFragmentBinding binding;
    private ArrayList<Event> mEvents;
    private EventListAdapter adapter;
    private static final String ARG_EVENTS = "events";

    public EventListFragment() {
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = EventListFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEvents = getArguments().getParcelableArrayList(ARG_EVENTS);
            adapter = new EventListAdapter(getActivity(), mEvents, getActivity());
            setListAdapter(adapter);
        }
    }

    public static EventListFragment newInstance(ArrayList<Event> events){
        EventListFragment fragment = new EventListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_EVENTS, events);
        fragment.setArguments(args);
        return fragment;
    }
}
