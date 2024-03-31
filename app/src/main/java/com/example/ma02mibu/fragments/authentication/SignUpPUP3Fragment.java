package com.example.ma02mibu.fragments.authentication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.model.Category;
import com.example.ma02mibu.model.EventType;

import java.util.ArrayList;

public class SignUpPUP3Fragment extends Fragment {

    private ArrayList<Category> categories = new ArrayList<>();
    private ArrayList<EventType> eventTypes = new ArrayList<>();
    public SignUpPUP3Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SignUpPUP3Fragment.
     */
    public static SignUpPUP3Fragment newInstance() {
        SignUpPUP3Fragment fragment = new SignUpPUP3Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareCategory();
        prepareEventTypes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up_p_u_p3, container, false);

        ((TimePicker) view.findViewById(R.id.mondayFromTime)).setIs24HourView(true);
        ((TimePicker) view.findViewById(R.id.mondayToTime)).setIs24HourView(true);
        ((TimePicker) view.findViewById(R.id.tuesdayFromTime)).setIs24HourView(true);
        ((TimePicker) view.findViewById(R.id.tuesdayToTime)).setIs24HourView(true);
        ((TimePicker) view.findViewById(R.id.wednesdayFromTime)).setIs24HourView(true);
        ((TimePicker) view.findViewById(R.id.wednesdayToTime)).setIs24HourView(true);
        ((TimePicker) view.findViewById(R.id.thursdayFromTime)).setIs24HourView(true);
        ((TimePicker) view.findViewById(R.id.thursdayToTime)).setIs24HourView(true);
        ((TimePicker) view.findViewById(R.id.fridayFromTime)).setIs24HourView(true);
        ((TimePicker) view.findViewById(R.id.fridayToTime)).setIs24HourView(true);
        ((TimePicker) view.findViewById(R.id.saturdayFromTime)).setIs24HourView(true);
        ((TimePicker) view.findViewById(R.id.saturdayToTime)).setIs24HourView(true);
        ((TimePicker) view.findViewById(R.id.sundayFromTime)).setIs24HourView(true);
        ((TimePicker) view.findViewById(R.id.sundayToTime)).setIs24HourView(true);

        Button btnNext = view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(SignUpPUP4Fragment.newInstance(categories, eventTypes), getActivity(), true, R.id.authenticationFragmentContainer, "signUpPUP");
            }
        });

        return view;
    }
    private void prepareCategory(){
        categories.add(new Category(1L, "Ime1", "Opis"));
        categories.add(new Category(2L, "Ime2", "Opis"));
        categories.add(new Category(3L, "Ime3", "Opis"));
        categories.add(new Category(4L, "Ime4", "Opis"));
        categories.add(new Category(5L, "Ime5", "Opis recimo da je ovaj malko duzi od ostalih da vidim kako ce da se wtapuje na ostale ulaze"));


        categories.add(new Category(4L, "Ime4", "Opis"));
        categories.add(new Category(4L, "Ime4", "Opis"));
        categories.add(new Category(4L, "Ime4", "Opis"));
        categories.add(new Category(4L, "Ime4", "Opis"));
        categories.add(new Category(4L, "Ime4", "Opis"));
        categories.add(new Category(4L, "Ime4", "Opis"));
        categories.add(new Category(4L, "Ime4", "Opis"));
    }

    private void prepareEventTypes(){
        eventTypes.add(new EventType(1L, "EventType1", "Some description of EventType 1"));
        eventTypes.add(new EventType(2L, "EventType2", "Some description of EventType 2"));
        eventTypes.add(new EventType(3L, "EventType3", "Some description of EventType 3"));
        eventTypes.add(new EventType(4L, "EventType4", "Some description of EventType 4"));
        eventTypes.add(new EventType(5L, "EventType5", "Some description of EventType 5"));
        eventTypes.add(new EventType(6L, "EventType6", "Some description of EventType 6"));

        eventTypes.add(new EventType(6L, "EventType6", "Some description of EventType 6"));
        eventTypes.add(new EventType(6L, "EventType6", "Some description of EventType 6"));
        eventTypes.add(new EventType(6L, "EventType6", "Some description of EventType 6"));
        eventTypes.add(new EventType(6L, "EventType6", "Some description of EventType 6"));
        eventTypes.add(new EventType(6L, "EventType6", "Some description of EventType 6"));
        eventTypes.add(new EventType(6L, "EventType6", "Some description of EventType 6"));
    }
}