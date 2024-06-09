package com.example.ma02mibu.fragments.events;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;

import com.example.ma02mibu.databinding.FragmentEventPageBinding;
import com.example.ma02mibu.fragments.employees.EmployeeListFragment;


public class EventPageFragment extends Fragment {

    private FragmentEventPageBinding binding;

    public EventPageFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        FragmentTransition.to(MyEventListFragment.newInstance(), getActivity(),
                true, R.id.scroll_events_list, "eventsPage");
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
