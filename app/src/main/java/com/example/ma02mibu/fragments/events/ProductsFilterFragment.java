package com.example.ma02mibu.fragments.events;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.adapters.ProductFilterAdapter;
import com.example.ma02mibu.adapters.ServiceListAdapter;
import com.example.ma02mibu.databinding.FragmentProductsFilterBinding;
import com.example.ma02mibu.model.Event;
import com.example.ma02mibu.model.EventTypeDTO;
import com.example.ma02mibu.model.Product;


import com.example.ma02mibu.model.ProductDAO;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.example.ma02mibu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProductsFilterFragment extends ListFragment {
    private FragmentProductsFilterBinding binding;
    private ArrayList<ProductDAO> mProducts;

    private ArrayList<ProductDAO> mProductsBackup;
    private ProductFilterAdapter adapter;

    private FirebaseAuth auth;

    private String suggestions;

    private String eventCreatorId;

    private ArrayList<Event> events;
    private ArrayList<String> typeList;

    private ProductDAO selectedItem;
    private static final String ARG_PARAM = "param";

    public static ArrayList<Event> eventsFake = new ArrayList<>();
    public static ProductsFilterFragment newInstance(ArrayList<ProductDAO> products){
        ProductsFilterFragment fragment = new ProductsFilterFragment();
        Bundle args = new Bundle();

        args.putParcelableArrayList(ARG_PARAM, products);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("ShopApp", "onCreate Products List Fragment");
        if (getArguments() != null) {
            mProducts = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new ProductFilterAdapter(getActivity(), getActivity(), mProducts);
            this.mProductsBackup = new ArrayList<>(mProducts);
            adapter.notifyDataSetChanged();
            setListAdapter(adapter);
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            eventCreatorId = user.getEmail();

        }

        Log.i("ShopApp", "onCreateView Products List Fragment");
        binding = FragmentProductsFilterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        auth = FirebaseAuth.getInstance();
        Button searchButton = binding.btnFilters;
        searchButton.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.FullScreenBottomSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_filter, null);
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
            getEventList(dialogView);
        });

        Event event1 =
                new Event(1L,"y", "turica", "x", 3, "x", true, "x", "test");
        Event event2 =
                new Event(2L,"y",  "idemooo", "x", 3, "x", true, "x", "test");
        eventsFake.add(event1);
        eventsFake.add(event2);

        Button eventButton = binding.btnFav;
        searchButton.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.FullScreenBottomSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_filter, null);
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();

            // Use ViewTreeObserver to wait until the dialog view is fully displayed
            ViewTreeObserver vto = dialogView.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // Remove the listener to avoid multiple calls

                    dialogView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    // Now that the dialog view is fully displayed, fetch event list
                    getEventList(dialogView);
                }
            });
        });



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void getEventList(@NonNull View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events").whereEqualTo("email", eventCreatorId)
                .get()
                .addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
                    ArrayList<Event> itemList = new ArrayList<>();
                    ArrayList<String> typeList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Event myItem = documentSnapshot.toObject(Event.class);
                        itemList.add(myItem);
                        typeList.add(myItem.getName());
                        Log.d("MyTag", "Added item with name: " + myItem.getName());
                        Log.d("MONKE", "MONKE");
                    }
                    if (!itemList.isEmpty()) {
                        this.events = itemList;
                        this.typeList = typeList;
                    } else {
                        typeList.add("NONE");
                        this.events = itemList;
                        this.typeList = typeList;

                    }
                    Spinner spinnerEventType = view.findViewById(R.id.spinner_event_typeX);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, typeList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerEventType.setAdapter(adapter);
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //getEventList(view);

    }
/*
    private void searchService(String name, String employeeName, Integer minPrice, Integer maxPrice, String eventType, boolean availableToBuy){
        mServices = new ArrayList<>(mServicesBackup);
        if (!name.isEmpty())
            mServices.removeIf(s -> !s.getName().toLowerCase().contains(name.toLowerCase()));
        if (!employeeName.isEmpty())
            mServices.removeIf(s -> !s.containsPerson(employeeName));
        if(minPrice != 0 || maxPrice != 0) {
            mServices.removeIf(s -> minPrice > s.getPriceByHour());
            mServices.removeIf(s -> maxPrice < s.getPriceByHour());
        }
        if(!eventType.isEmpty())
            mServices.removeIf(s -> !s.containsEventType(eventType));
        if(availableToBuy)
            mServices.removeIf(s -> !s.isAvailableToBuy());
        adapter = new ServiceListAdapter(getActivity(), mServices, getActivity(), false, null);
        setListAdapter(adapter);
    }
    private void resetServices(){
        mServices = new ArrayList<>(mServicesBackup);
        adapter = new ServiceListAdapter(getActivity(), mServices, getActivity(), false, null);
        setListAdapter(adapter);
    } */






}
