package com.example.ma02mibu.fragments.adminsManagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.ma02mibu.R;
import com.example.ma02mibu.adapters.adminsManagment.SubcategoryCheckableListAdapter;
import com.example.ma02mibu.adapters.adminsManagment.SubcategoryListAdapter;
import com.example.ma02mibu.model.EventType;
import com.example.ma02mibu.model.SubCategory;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventTypeEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventTypeEditFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private boolean isEventTypeNew;
    private EventType eventType;
    private SubcategoryCheckableListAdapter subcategoryCheckableListAdapter;
    private ArrayList<SubCategory> subCategories = new ArrayList<>();

    public EventTypeEditFragment() { }

    public static EventTypeEditFragment newInstance(boolean param1, EventType param2) {
        EventTypeEditFragment fragment = new EventTypeEditFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        args.putParcelable(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isEventTypeNew = getArguments().getBoolean(ARG_PARAM1);
            eventType = getArguments().getParcelable(ARG_PARAM2);
        }
        createSubCategories();
        subcategoryCheckableListAdapter = new SubcategoryCheckableListAdapter(getActivity(), subCategories);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_type_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ((EditText) view.findViewById(R.id.eventTypeName)).setText(eventType.getName());
        ((EditText) view.findViewById(R.id.eventTypeName)).setEnabled(eventType.getStatus() == EventType.EVENTTYPESTATUS.ACTIVE || isEventTypeNew);

        ((EditText) view.findViewById(R.id.eventTypeDescription)).setText(eventType.getDescription());

        ((ListView) view.findViewById(R.id.subcategoryListView)).setAdapter(subcategoryCheckableListAdapter);

        ((Button) view.findViewById(R.id.saveEventTypeChanges)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });

        Button deactivateEventType = view.findViewById(R.id.deactivateEventType);
        deactivateEventType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
        deactivateEventType.setEnabled(!isEventTypeNew || eventType.getStatus()== EventType.EVENTTYPESTATUS.ACTIVE);

    }
    private void createSubCategories(){
        subCategories.add(new SubCategory(1L, 1L, "Ime podkategorije 1", "Opis podkategorije 1", SubCategory.SUBCATEGORYTYPE.PROIZVOD));
        subCategories.add(new SubCategory(2L, 1L, "Ime podkategorije 2", "Opis podkategorije 2", SubCategory.SUBCATEGORYTYPE.USLUGA));
        subCategories.add(new SubCategory(3L, 3L, "Ime podkategorije 3", "Opis podkategorije 3", SubCategory.SUBCATEGORYTYPE.PROIZVOD));
        subCategories.add(new SubCategory(4L, 3L, "Ime podkategorije 4", "Opis podkategorije 4", SubCategory.SUBCATEGORYTYPE.PROIZVOD));
        subCategories.add(new SubCategory(5L, 3L, "Ime podkategorije 5", "Opis podkategorije 5", SubCategory.SUBCATEGORYTYPE.USLUGA));
        subCategories.add(new SubCategory(6L, 2L, "Ime podkategorije 6", "Opis podkategorije 6", SubCategory.SUBCATEGORYTYPE.USLUGA));
        subCategories.add(new SubCategory(7L, 2L, "Ime podkategorije 7", "Opis podkategorije 7", SubCategory.SUBCATEGORYTYPE.USLUGA));
        subCategories.add(new SubCategory(8L, 1L, "Ime podkategorije 8", "Opis podkategorije 8", SubCategory.SUBCATEGORYTYPE.PROIZVOD));
    }
}