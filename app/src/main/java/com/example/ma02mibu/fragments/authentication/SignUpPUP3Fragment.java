package com.example.ma02mibu.fragments.authentication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.databinding.FragmentSignUpPUP3Binding;
import com.example.ma02mibu.model.Category;
import com.example.ma02mibu.model.EventType;
import com.example.ma02mibu.model.Owner;
import com.example.ma02mibu.model.WorkSchedule;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;

public class SignUpPUP3Fragment extends Fragment {

    private static final String ARG_PARAM1 = "owner";
    private static final String ARG_PARAM2 = "password";
    private Owner owner;
    private String password;

    private TimePicker mondayFromTime;
    private TimePicker mondayToTime;
    private TimePicker tuesdayFromTime;
    private TimePicker tuesdayToTime;
    private TimePicker wednesdayFromTime;
    private TimePicker wednesdayToTime;
    private TimePicker thursdayFromTime;
    private TimePicker thursdayToTime;
    private TimePicker fridayFromTime;
    private TimePicker fridayToTime;
    private TimePicker saturdayFromTime;
    private TimePicker saturdayToTime;
    private TimePicker sundayFromTime;
    private TimePicker sundayToTime;

    private CheckBox mondayCheckBox;
    private CheckBox tuesdayCheckBox;
    private CheckBox wednesdayCheckBox;
    private CheckBox thursdayCheckBox;
    private CheckBox fridayCheckBox;
    private CheckBox saturdayCheckBox;
    private CheckBox sundayCheckBox;

    private ArrayList<Category> categories = new ArrayList<>();
    private ArrayList<EventType> eventTypes = new ArrayList<>();
    public SignUpPUP3Fragment() { }

    public static SignUpPUP3Fragment newInstance(Owner param1, String password) {
        SignUpPUP3Fragment fragment = new SignUpPUP3Fragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, password);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            owner = getArguments().getParcelable(ARG_PARAM1);
            password = getArguments().getString(ARG_PARAM2);
        }
        prepareCategory();
        prepareEventTypes();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        com.example.ma02mibu.databinding.FragmentSignUpPUP3Binding binding = FragmentSignUpPUP3Binding.inflate(inflater, container, false);

        mondayFromTime = binding.mondayFromTime;
        mondayToTime = binding.mondayToTime;
        tuesdayFromTime = binding.tuesdayFromTime;
        tuesdayToTime = binding.tuesdayToTime;
        wednesdayFromTime = binding.wednesdayFromTime;
        wednesdayToTime = binding.wednesdayToTime;
        thursdayFromTime = binding.thursdayFromTime;
        thursdayToTime = binding.thursdayToTime;
        fridayFromTime = binding.fridayFromTime;
        fridayToTime = binding.fridayToTime;
        saturdayFromTime = binding.saturdayFromTime;
        saturdayToTime = binding.saturdayToTime;
        sundayFromTime = binding.sundayFromTime;
        sundayToTime = binding.sundayToTime;

        mondayCheckBox = binding.mondayCheckBox;
        tuesdayCheckBox = binding.tuesdayCheckBox;
        wednesdayCheckBox = binding.wednesdayCheckBox;
        thursdayCheckBox = binding.thursdayCheckBox;
        fridayCheckBox = binding.fridayCheckBox;
        saturdayCheckBox = binding.saturdayCheckBox;
        sundayCheckBox = binding.sundayCheckBox;

        set24HourView();

        setDefaultWorkingHours();

        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button btnNext = view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(v -> {
            if(!validateFields()){
                return;
            }
            owner.getMyCompany().setWorkSchedule(setWorkSchedule());
            FragmentTransition.to(SignUpPUP4Fragment.newInstance(owner, password, categories, eventTypes), requireActivity(), true, R.id.authenticationFragmentContainer, "signUpPUP");
        });
    }

    private WorkSchedule setWorkSchedule() {
        WorkSchedule ret = new WorkSchedule();
        ret.setStartDay(null);
        ret.setEndDay(null);

        if(mondayCheckBox.isChecked()){
            ret.setWorkTime(DayOfWeek.MONDAY, LocalTime.of(mondayFromTime.getHour(), mondayFromTime.getMinute()), LocalTime.of(mondayToTime.getHour(), mondayToTime.getMinute()));
        }
        if(tuesdayCheckBox.isChecked()){
            ret.setWorkTime(DayOfWeek.TUESDAY, LocalTime.of(tuesdayFromTime.getHour(), tuesdayFromTime.getMinute()), LocalTime.of(tuesdayToTime.getHour(), tuesdayToTime.getMinute()));
        }
        if(wednesdayCheckBox.isChecked()){
            ret.setWorkTime(DayOfWeek.WEDNESDAY, LocalTime.of(wednesdayFromTime.getHour(), wednesdayFromTime.getMinute()), LocalTime.of(wednesdayToTime.getHour(), wednesdayToTime.getMinute()));
        }
        if(thursdayCheckBox.isChecked()){
            ret.setWorkTime(DayOfWeek.THURSDAY, LocalTime.of(thursdayFromTime.getHour(), thursdayFromTime.getMinute()), LocalTime.of(thursdayToTime.getHour(), thursdayToTime.getMinute()));
        }
        if(fridayCheckBox.isChecked()){
            ret.setWorkTime(DayOfWeek.FRIDAY, LocalTime.of(fridayFromTime.getHour(), fridayFromTime.getMinute()), LocalTime.of(fridayToTime.getHour(), fridayToTime.getMinute()));
        }
        if(saturdayCheckBox.isChecked()){
            ret.setWorkTime(DayOfWeek.SATURDAY, LocalTime.of(saturdayFromTime.getHour(), saturdayFromTime.getMinute()), LocalTime.of(saturdayToTime.getHour(), saturdayToTime.getMinute()));
        }
        if(sundayCheckBox.isChecked()){
            ret.setWorkTime(DayOfWeek.SUNDAY, LocalTime.of(sundayFromTime.getHour(), sundayFromTime.getMinute()), LocalTime.of(sundayToTime.getHour(), sundayToTime.getMinute()));
        }

        return ret;
    }

    private boolean validateFields(){
        boolean valid = true;
        if(mondayCheckBox.isChecked() &&
                (mondayFromTime.getHour() > mondayToTime.getHour() ||
                        (mondayFromTime.getHour() == mondayToTime.getHour() &&
                                mondayFromTime.getMinute() >= mondayToTime.getMinute()))){
            Toast.makeText(getContext(), "Monday: From time can't be greater than to time",Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if(tuesdayCheckBox.isChecked() &&
                (tuesdayFromTime.getHour() > tuesdayToTime.getHour() ||
                        (tuesdayFromTime.getHour() == tuesdayToTime.getHour() &&
                                tuesdayFromTime.getMinute() >= tuesdayToTime.getMinute()))){
            Toast.makeText(getContext(), "Tuesday: From time can't be greater than to time",Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if(wednesdayCheckBox.isChecked() &&
                (wednesdayFromTime.getHour() > wednesdayToTime.getHour() ||
                        (wednesdayFromTime.getHour() == wednesdayToTime.getHour() &&
                                wednesdayFromTime.getMinute() >= wednesdayToTime.getMinute()))){
            Toast.makeText(getContext(), "Wednesday: From time can't be greater than to time",Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if(thursdayCheckBox.isChecked() &&
                (thursdayFromTime.getHour() > thursdayToTime.getHour() ||
                        (thursdayFromTime.getHour() == thursdayToTime.getHour() &&
                                thursdayFromTime.getMinute() >= thursdayToTime.getMinute()))){
            Toast.makeText(getContext(), "Thursday: From time can't be greater than to time",Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if(fridayCheckBox.isChecked() &&
                (fridayFromTime.getHour() > fridayToTime.getHour() ||
                        (fridayFromTime.getHour() == fridayToTime.getHour() &&
                                fridayFromTime.getMinute() >= fridayToTime.getMinute()))){
            Toast.makeText(getContext(), "Friday: From time can't be greater than to time",Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if(saturdayCheckBox.isChecked() &&
                (saturdayFromTime.getHour() > saturdayToTime.getHour() ||
                        (saturdayFromTime.getHour() == saturdayToTime.getHour() &&
                                saturdayFromTime.getMinute() >= saturdayToTime.getMinute()))){
            Toast.makeText(getContext(), "Saturday: From time can't be greater than to time",Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if(sundayCheckBox.isChecked() &&
                (sundayFromTime.getHour() > sundayToTime.getHour() ||
                        (sundayFromTime.getHour() == sundayToTime.getHour() &&
                                sundayFromTime.getMinute() >= sundayToTime.getMinute()))){
            Toast.makeText(getContext(), "Sunday: From time can't be greater than to time",Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

    private void set24HourView() {
        mondayFromTime.setIs24HourView(true);
        mondayToTime.setIs24HourView(true);
        tuesdayFromTime.setIs24HourView(true);
        tuesdayToTime.setIs24HourView(true);
        wednesdayFromTime.setIs24HourView(true);
        wednesdayToTime.setIs24HourView(true);
        thursdayFromTime.setIs24HourView(true);
        thursdayToTime.setIs24HourView(true);
        fridayFromTime.setIs24HourView(true);
        fridayToTime.setIs24HourView(true);
        saturdayFromTime.setIs24HourView(true);
        saturdayToTime.setIs24HourView(true);
        sundayFromTime.setIs24HourView(true);
        sundayToTime.setIs24HourView(true);
    }

    private void setDefaultWorkingHours(){
        mondayFromTime.setHour(8);
        mondayFromTime.setMinute(0);
        mondayToTime.setHour(16);
        mondayToTime.setMinute(0);

        tuesdayFromTime.setHour(8);
        tuesdayFromTime.setMinute(0);
        tuesdayToTime.setHour(16);
        tuesdayToTime.setMinute(0);

        wednesdayFromTime.setHour(8);
        wednesdayFromTime.setMinute(0);
        wednesdayToTime.setHour(16);
        wednesdayToTime.setMinute(0);

        thursdayFromTime.setHour(8);
        thursdayFromTime.setMinute(0);
        thursdayToTime.setHour(16);
        thursdayToTime.setMinute(0);

        fridayFromTime.setHour(8);
        fridayFromTime.setMinute(0);
        fridayToTime.setHour(16);
        fridayToTime.setMinute(0);

        saturdayFromTime.setHour(8);
        saturdayFromTime.setMinute(0);
        saturdayToTime.setHour(16);
        saturdayToTime.setMinute(0);

        sundayFromTime.setHour(8);
        sundayFromTime.setMinute(0);
        sundayToTime.setHour(16);
        sundayToTime.setMinute(0);
    }

    private void prepareCategory(){
        categories.add(new Category("Ime1", "Opis"));
        categories.add(new Category("Ime2", "Opis"));
        categories.add(new Category("Ime3", "Opis"));
        categories.add(new Category("Ime4", "Opis"));
        categories.add(new Category("Ime5", "Opis recimo da je ovaj malko duzi od ostalih da vidim kako ce da se wtapuje na ostale ulaze"));


        categories.add(new Category("Ime4", "Opis"));
        categories.add(new Category("Ime4", "Opis"));
        categories.add(new Category("Ime4", "Opis"));
        categories.add(new Category("Ime4", "Opis"));
        categories.add(new Category("Ime4", "Opis"));
        categories.add(new Category("Ime4", "Opis"));
        categories.add(new Category("Ime4", "Opis"));
    }

    private void prepareEventTypes(){
        eventTypes.add(new EventType(1L, "EventType1", "Some description of EventType 1", EventType.EVENTTYPESTATUS.DEACTIVATED));
        eventTypes.add(new EventType(2L, "EventType2", "Some description of EventType 2", EventType.EVENTTYPESTATUS.DEACTIVATED));
        eventTypes.add(new EventType(3L, "EventType3", "Some description of EventType 3", EventType.EVENTTYPESTATUS.ACTIVE));
        eventTypes.add(new EventType(4L, "EventType4", "Some description of EventType 4", EventType.EVENTTYPESTATUS.ACTIVE));
        eventTypes.add(new EventType(5L, "EventType5", "Some description of EventType 5", EventType.EVENTTYPESTATUS.ACTIVE));
        eventTypes.add(new EventType(6L, "EventType6", "Some description of EventType 6", EventType.EVENTTYPESTATUS.DEACTIVATED));
    }
}