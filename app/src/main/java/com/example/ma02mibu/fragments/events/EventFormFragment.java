package com.example.ma02mibu.fragments.events;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.databinding.EventListFragmentBinding;
import com.example.ma02mibu.databinding.FragmentEventFormBinding;
import com.example.ma02mibu.databinding.FragmentProductsFilterBinding;
import com.example.ma02mibu.fragments.employees.EmployeeListFragment;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.Event;
import com.example.ma02mibu.model.EventType;
import com.example.ma02mibu.model.EventTypeDTO;
import com.example.ma02mibu.model.WorkSchedule;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;

import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.example.ma02mibu.model.EventOrganizer;
import com.example.ma02mibu.model.Owner;
import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFormFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<EventTypeDTO> itemList;

    private ArrayList<String> typeList;

    private FragmentEventFormBinding binding;

    private FirebaseAuth auth;

    private String suggestions;
    private String eventCreatorId;

    public EventFormFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventFormFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventFormFragment newInstance(String param1, String param2) {
        EventFormFragment fragment = new EventFormFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            eventCreatorId = user.getEmail();

        }
        //getEventTypeList();
        binding = FragmentEventFormBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        auth = FirebaseAuth.getInstance();
        Button suggestionButton = binding.suggestion;
        addToBackStack(getActivity(),"createEventPage");
        suggestionButton.setOnClickListener(v -> {

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.FullScreenBottomSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_suggestion, null);

            TextView textViewTest = dialogView.findViewById(R.id.textViewTEST);
            String dynamicData = getSubcategoriesByName(binding.spinnerEventType.getSelectedItem().toString());
            textViewTest.setText(dynamicData);

            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
        });
        Button budgetButton = binding.budget;
        budgetButton.setOnClickListener(v -> {
            FragmentTransition.to(EventBudgetFragment.newInstance("",""), getActivity(),
                    true, R.id.frameLayout2, "addBudgetPage");
        });
        Button btnRegister = binding.button1;
        btnRegister.setOnClickListener(v -> {
            try {
                addEvent();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getEventTypeList(view);

    }

    public void addToBackStack(FragmentActivity activity, String backStackTag) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Add the existing fragment's view to the back stack
        transaction.addToBackStack(backStackTag);

        transaction.commit();
    }

    private boolean isValidDateFormat(String date) {
        // Define the pattern for mm.dd.yyyy format
        String pattern = "\\d{2}\\.\\d{2}\\.\\d{4}";
        // Check if the date matches the pattern
        return date.matches(pattern);
    }

    private void addEvent() throws InterruptedException {

        String fName = binding.editTextText2.getText().toString();
        String fDescription = binding.editTextText3.getText().toString();
        String fParticipantNumber = binding.editTextText4.getText().toString();
        String fLocation = binding.editTextText5.getText().toString();
        String fPrivacy = binding.checkBox.isChecked() ? "true" : "false";
        String fDate= binding.editTextText6.getText().toString();
        String fType = binding.spinnerEventType.getSelectedItem().toString();

        if (fName.isEmpty() || fDescription.isEmpty() || fParticipantNumber.isEmpty() || fLocation.isEmpty() || fDate.isEmpty() || fType.isEmpty()) {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Fields empty");
            alertDialog.setMessage("Fields can not be left empty!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();
            return;
        }

        if (!isValidDateFormat(fDate)) {
            // Date format is not valid, show an alert dialog:
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Invalid Date Format");
            alertDialog.setMessage("Please make sure the date is in the format mm.dd.yyyy.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // :()
                }
            });
            alertDialog.show();
            return;
        }

        Event e = new Event(1L, fType, fName,fDescription, Integer.parseInt(fParticipantNumber), fLocation, Boolean.parseBoolean(fPrivacy), fDate, this.eventCreatorId);

        createEvent(e);

    }

    private String getSubcategoriesByName(String name){
        for(EventTypeDTO type:this.itemList){
            if(type.getName().equals(name)){
                return type.getSubcategories();
            }
        }
        return "YOLO!";
    }

    private void createEvent(Event event) {
        Log.d(TAG, "createEvent:" + event.getName());
        CloudStoreUtil.insertEventNew(event);
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Success");
        alertDialog.setMessage("Event created successfully!");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // You can add actions here if needed
            }
        });
        alertDialog.show();
    }


    public void getEventTypeList(@NonNull View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("eventTypeTestKT2")
                .get()
                .addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
                    ArrayList<EventTypeDTO> itemList = new ArrayList<>();
                    ArrayList<String> typeList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        EventTypeDTO myItem = documentSnapshot.toObject(EventTypeDTO.class);
                        itemList.add(myItem);
                        typeList.add(myItem.getName());
                        Log.d("MyTag", "Added item with name: " + myItem.getName());
                        Log.d("MONKE", "MONKE");
                    }
                    if (!itemList.isEmpty()) {
                        this.itemList = itemList;
                        this.typeList = typeList;
                    } else {
                        EventTypeDTO other = new EventTypeDTO("OTHER", "OTHER", "OTHER");
                        itemList.add(other);
                        typeList.add(other.getName());
                        this.itemList = itemList;
                        this.typeList = typeList;

                    }
                    Spinner spinnerEventType = view.findViewById(R.id.spinner_event_type);
                    //String[] eventTypes = {"Event Type 1", "Event Type 2", "Event Type 3"};
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, this.typeList.toArray(new String[0]));
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerEventType.setAdapter(adapter);
                });
    }






}