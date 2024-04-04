package com.example.ma02mibu.fragments.events;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.adapters.ProductFilterAdapter;
import com.example.ma02mibu.databinding.FragmentProductsFilterBinding;
import com.example.ma02mibu.model.Event;
import com.example.ma02mibu.model.Product;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.example.ma02mibu.R;

import java.util.ArrayList;

public class ProductsFilterFragment extends ListFragment {
    private FragmentProductsFilterBinding binding;
    private ArrayList<Product> mProducts;
    private ProductFilterAdapter adapter;
    private static final String ARG_PARAM = "param";

    public static ArrayList<Event> eventsFake = new ArrayList<>();
    public static ProductsFilterFragment newInstance(ArrayList<Product> products){
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
            adapter = new ProductFilterAdapter(getActivity(), mProducts);
            setListAdapter(adapter);
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("ShopApp", "onCreateView Products List Fragment");
        binding = FragmentProductsFilterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button searchButton = binding.btnFilters;
        searchButton.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.FullScreenBottomSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_filter, null);
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
        });

        Event event1 =
                new Event(1L, "turica", "x", 3, "x", true, "x");
        Event event2 =
                new Event(2L, "idemooo", "x", 3, "x", true, "x");
        eventsFake.add(event1);
        eventsFake.add(event2);

        Button eventButton = binding.btnFav;
        eventButton.setOnClickListener(v -> {
            FragmentTransition.to(EventListFragment.newInstance(eventsFake), getActivity(),
                    true, R.id.scroll_products_list, "myEventsPage");
        });



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
