package com.example.ma02mibu.fragments.products;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.adapters.ProductListAdapter;
import com.example.ma02mibu.databinding.FragmentProductsListBinding;
import com.example.ma02mibu.model.Product;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class ProductsListFragment extends ListFragment {
    private FragmentProductsListBinding binding;
    private ArrayList<Product> mProducts;
    private ArrayList<String> categories;
    private ArrayList<String> subCategories;
    private ProductListAdapter adapter;
    private static final String ARG_PARAM = "param";
    public static ProductsListFragment newInstance(ArrayList<Product> products){
        ProductsListFragment fragment = new ProductsListFragment();
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
            adapter = new ProductListAdapter(getActivity(), mProducts);
            setListAdapter(adapter);
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("ShopApp", "onCreateView Products List Fragment");
        binding = FragmentProductsListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button searchButton = binding.searchButton;
        searchButton.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.FullScreenBottomSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.product_search_dialog, null);
            bottomSheetDialog.setContentView(dialogView);
            Spinner spinnerCategories = bottomSheetDialog.findViewById(R.id.product_category_search);
            Spinner spinnerSubCategories = bottomSheetDialog.findViewById(R.id.product_subcategory_search);
            spinnerCategories.setAdapter(setCategoriesSpinnerAdapter());
            spinnerSubCategories.setAdapter(setSubCategoriesSpinnerAdapter());
            bottomSheetDialog.show();
        });

        Button newProductButton = binding.newProductButton;
        newProductButton.setOnClickListener(v -> {
            FragmentTransition.to(NewProduct.newInstance(), getActivity(),
                    true, R.id.scroll_products_list, "newProductPage");
        });

        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private ArrayAdapter<String> setCategoriesSpinnerAdapter(){
        categories = new ArrayList<>();
        categories.add("Category 1");
        categories.add("Category 2");
        categories.add("Category 3");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private ArrayAdapter<String> setSubCategoriesSpinnerAdapter(){
        subCategories = new ArrayList<>();
        subCategories.add("Sub-Category 1");
        subCategories.add("Sub-Category 2");
        subCategories.add("Sub-Category 3");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, subCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
}
