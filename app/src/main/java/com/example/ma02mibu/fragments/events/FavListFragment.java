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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.FavItemsAdapter;
import com.example.ma02mibu.adapters.GuestAdapter;
import com.example.ma02mibu.adapters.MyEventListAdapter;
import com.example.ma02mibu.databinding.FragmentEventAgendaBinding;
import com.example.ma02mibu.databinding.FragmentEventGuestListBinding;
import com.example.ma02mibu.databinding.FragmentFavListBinding;
import com.example.ma02mibu.model.Event;
import com.example.ma02mibu.model.Guest;
import com.example.ma02mibu.model.GuestListDTO;
import com.example.ma02mibu.model.ProductDAO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class FavListFragment extends Fragment  {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FirebaseAuth auth;

    private Event mEvent;
    private String ownerRefId;

    private String email;
    private FragmentFavListBinding binding;

    private EditText nameEditText;
    private EditText ageEditText;
    private CheckBox invitedCheckBox;
    private CheckBox acceptedCheckBox;
    private EditText specialEditText;
    private RecyclerView recyclerView;

    private List<ProductDAO> personList;
    private FavItemsAdapter personAdapter;

    private FragmentActivity backup;



    //private ArrayList<Service> services;
    public FavListFragment() {
        // Required empty public constructor
    }
    public static FavListFragment newInstance(Event param1, String param2) {
        FavListFragment fragment = new FavListFragment();
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
        //loadFavourites();
    }

    public void onCreateClick() {
        CollectionReference eventsCollection = FirebaseFirestore.getInstance().collection("favourites");

        // Create a new instance of your Event class
        GuestListDTO gl = new GuestListDTO(); // Assuming you have a GuestListDTO class

        // Query documents where the "name" field is equal to "test"
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
                .update("guests", personList)
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
    public void onEditClick(Guest guest) {
        nameEditText.setText(guest.getName());
        ageEditText.setText(String.valueOf(guest.getAge()));
        invitedCheckBox.setChecked(guest.getInvited());
        acceptedCheckBox.setChecked(guest.getHasAccepted());
        specialEditText.setText(guest.getSpecial());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            ownerRefId = user.getUid();
            email = user.getEmail();
        }

        // Inflate the layout for this fragment
        binding = FragmentFavListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        backup = getActivity();

        nameEditText = view.findViewById(R.id.editTextName);
        ageEditText = view.findViewById(R.id.editTextAge);
        invitedCheckBox = view.findViewById(R.id.checkBoxInvited);
        acceptedCheckBox = view.findViewById(R.id.checkBoxAccepted);
        specialEditText = view.findViewById(R.id.editTextSpecial);



        loadFavourites();
        return view;
    }

        private void loadFavourites() {
            CloudStoreUtil.getFavourites(email, new CloudStoreUtil.FavouritesCallback() {
                @Override
                public void onSuccess(ArrayList<ProductDAO> itemList) {
                    // Handle the retrieved list of items (e.g., display them in UI)
                    personList = new ArrayList<>(itemList);
                    Log.e("FIREBASE MONKEY", String.valueOf(personList.size()));
                    personAdapter = new FavItemsAdapter(personList, getActivity());
                    View view = binding.getRoot();
                    recyclerView = view.findViewById(R.id.recyclerViewFav);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(personAdapter);
                }

                @Override
                public void onFailure(Exception e) {
                    // Handle the failure (e.g., show an error message)
                    System.err.println("Error fetching documents: " + e.getMessage());
                }
            });
        }

    }






