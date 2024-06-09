package com.example.ma02mibu.fragments.events;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ma02mibu.R;
import com.example.ma02mibu.adapters.AgendaAdapter;
import com.example.ma02mibu.adapters.GuestAdapter;
import com.example.ma02mibu.databinding.FragmentEventAgendaBinding;
import com.example.ma02mibu.databinding.FragmentEventGuestListBinding;
import com.example.ma02mibu.model.AgendaItem;
import com.example.ma02mibu.model.Event;
import com.example.ma02mibu.model.Guest;
import com.example.ma02mibu.model.GuestListDTO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventAgendaFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Event mEvent;
    private String ownerRefId;
    private FragmentEventAgendaBinding binding;

    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText timespanEditText;
    private EditText locationEditText;

    private Button addButton;
    private Button createButton;
    private RecyclerView recyclerView;
    private List<AgendaItem> personList;
    private AgendaAdapter personAdapter;



    //private ArrayList<Service> services;
    public EventAgendaFragment() {
        // Required empty public constructor
    }
    public static EventAgendaFragment newInstance(Event param1, String param2) {
        EventAgendaFragment fragment = new EventAgendaFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEvent = getArguments().getParcelable(ARG_PARAM1);
            ownerRefId = getArguments().getString(ARG_PARAM2);
        }
    }

    public void onCreateClick() {
        CollectionReference eventsCollection = FirebaseFirestore.getInstance().collection("events");


        GuestListDTO gl = new GuestListDTO(); // Assuming you have a GuestListDTO class


        eventsCollection.whereEqualTo("name", mEvent.getName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            DocumentReference docRef = documentSnapshot.getReference();
                            updateDocument(docRef, gl);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FIREBASE MONKEY", "Error querying documents", e);
                    }
                });
    }

    private void updateDocument(DocumentReference documentReference, GuestListDTO gl) {
        // Update the document with the specified data
        documentReference
                .update("agenda", personList)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("FIREBASE MONKEY", "Persons added to event successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FIREBASE MONKEY", "Error adding persons to event", e);
                    }
                });
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEventAgendaBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        nameEditText = view.findViewById(R.id.editTextNameA);
        descriptionEditText = view.findViewById(R.id.editTextDescriptionA);
        timespanEditText = view.findViewById(R.id.editTextTimespanA);
        locationEditText = view.findViewById(R.id.editTextLocationA);
        addButton = view.findViewById(R.id.buttonAddA);
        createButton = view.findViewById(R.id.buttonCreateA);
        recyclerView = view.findViewById(R.id.recyclerView);

        createButton.setOnClickListener(v -> {
            onCreateClick();
        });


        personList = new ArrayList<>();
        personAdapter = new AgendaAdapter(personList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(personAdapter);



        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                String timespan = timespanEditText.getText().toString();
                String location = locationEditText.getText().toString();


                if (!name.isEmpty() && !description.isEmpty() && !timespan.isEmpty() && !location.isEmpty() ) {

                    AgendaItem person = new AgendaItem(name, description, timespan, location);
                    personList.add(person);
                    personAdapter.notifyDataSetChanged();
                    nameEditText.setText("");
                    descriptionEditText.setText("");
                    timespanEditText.setText("");
                    locationEditText.setText("");
                }
            }
        });
        return view;
    }


}
