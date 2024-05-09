package com.example.ma02mibu.fragments.adminsManagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.adminsManagment.SubcategoryCheckableListAdapter;
import com.example.ma02mibu.adapters.adminsManagment.SubcategoryListAdapter;
import com.example.ma02mibu.databinding.FragmentEventTypeEditBinding;
import com.example.ma02mibu.model.EventType;
import com.example.ma02mibu.model.Subcategory;

import java.util.ArrayList;
import java.util.Objects;

public class EventTypeEditFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private boolean isEventTypeNew;
    private EventType eventType;

    private EditText name;
    private EditText description;

    private ListView subcategoryListView;
    private SubcategoryCheckableListAdapter subcategoryCheckableListAdapter;
    private ArrayList<Subcategory> subcategories = new ArrayList<>();
    private ArrayList<String> selectedSubcategories = new ArrayList<>();

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
        subcategoryCheckableListAdapter = new SubcategoryCheckableListAdapter(getActivity(), subcategories);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentEventTypeEditBinding binding = FragmentEventTypeEditBinding.inflate(inflater, container, false);

        subcategoryListView = binding.subcategoryListView;
        name = binding.eventTypeName;
        description = binding.eventTypeDescription;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        subcategoryListView.setOnItemClickListener((parent, view1, position, id) -> {
            ((SubcategoryCheckableListAdapter) subcategoryListView.getAdapter()).toggleCheckedWithView(position, view1, parent);
            Subcategory temp = ((SubcategoryCheckableListAdapter) subcategoryListView.getAdapter()).getItem(position);
            if(selectedSubcategories.contains(temp.getDocumentRefId())){
                selectedSubcategories.remove(temp.getDocumentRefId());
            }else{
                selectedSubcategories.add(temp.getDocumentRefId());
            }
        });

        getSubcategories();

        if(!isEventTypeNew){
            name.setText(eventType.getName());
            name.setEnabled(false);

            selectedSubcategories = eventType.getSuggestedSubcategoryDocRefId();

            description.setText(eventType.getDescription());
            description.setEnabled(eventType.getStatus() == EventType.EVENTTYPESTATUS.ACTIVE);
            subcategoryListView.setEnabled(eventType.getStatus() == EventType.EVENTTYPESTATUS.ACTIVE);
        }

        Button saveEventType = view.findViewById(R.id.saveEventTypeChanges);
        saveEventType.setEnabled(isEventTypeNew || eventType.getStatus() == EventType.EVENTTYPESTATUS.ACTIVE);
        saveEventType.setOnClickListener(v -> {
            if(!fieldsAreValid()){
                return;
            }
            if(isEventTypeNew) {
                eventType.setName(name.getText().toString());
                eventType.setDescription(description.getText().toString());
                eventType.setStatus(EventType.EVENTTYPESTATUS.ACTIVE);
                eventType.setSuggestedSubcategoryDocRefId(selectedSubcategories);
                CloudStoreUtil.insertEventType(eventType);
            }else{
                eventType.setDescription(description.getText().toString());
                eventType.setSuggestedSubcategoryDocRefId(selectedSubcategories);
                CloudStoreUtil.updateEventType(eventType);
            }
            FragmentTransition.goBack(requireActivity(), "eventTypeManagement");
        });

        Button deactivateEventType = view.findViewById(R.id.deactivateEventType);
        deactivateEventType.setOnClickListener(v -> {
            eventType.setStatus(EventType.EVENTTYPESTATUS.DEACTIVATED);
            CloudStoreUtil.updateEventType(eventType);
        });
        deactivateEventType.setEnabled(!isEventTypeNew && eventType.getStatus() == EventType.EVENTTYPESTATUS.ACTIVE);
    }

    private void checkSubcategoriesOfExistingEventType() {
        for(String s: selectedSubcategories){
            for(int i = 0; i < subcategoryListView.getAdapter().getCount(); i++){
                if (Objects.equals(((Subcategory) subcategoryListView.getAdapter().getItem(i)).getDocumentRefId(), s)){
                    ((SubcategoryCheckableListAdapter) subcategoryListView.getAdapter()).toggleChecked(i);
                }
            }
        }
    }

    private boolean fieldsAreValid() {
        boolean valid = true;
        if(name.getText().toString().isEmpty()){
            name.setError("Name is required");
            valid = false;
        }
        else {
            name.setError(null);
        }
        if(description.getText().toString().isEmpty()){
            description.setError("Description is required");
            valid = false;
        }
        else{
            description.setError(null);
        }
        return valid;
    }

    private void getSubcategories(){
        CloudStoreUtil.selectSubcategories(result -> {
            subcategories = result;
            subcategoryCheckableListAdapter = new SubcategoryCheckableListAdapter(getActivity(), subcategories);
            subcategoryListView.setAdapter(subcategoryCheckableListAdapter);
            if(!isEventTypeNew){
                checkSubcategoriesOfExistingEventType();
            }
        });
    }
}