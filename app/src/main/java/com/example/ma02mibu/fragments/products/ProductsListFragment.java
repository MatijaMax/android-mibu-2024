package com.example.ma02mibu.fragments.products;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.ProductListAdapter;
import com.example.ma02mibu.databinding.FragmentProductsListBinding;
import com.example.ma02mibu.model.Product;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class ProductsListFragment extends ListFragment {
    private FragmentProductsListBinding binding;
    private ArrayList<Product> mProducts;
    private ArrayList<Product> mProductsBackup;
    private ArrayList<String> categories;
    private ArrayList<String> subCategories;
    private ProductListAdapter adapter;
    private static ArrayList<Product> products = new ArrayList<>();
    private static final String ARG_PARAM = "param";
    public static ProductsListFragment newInstance(){
        ProductsListFragment fragment = new ProductsListFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("ShopApp", "onCreate Products List Fragment");
        CloudStoreUtil.selectProducts(new CloudStoreUtil.ProductCallback(){
            @Override
            public void onCallback(ArrayList<Product> retrievedProducts) {
                if (retrievedProducts != null) {
                    mProducts = retrievedProducts;
                } else {
                    mProducts = new ArrayList<>();
                }
                mProductsBackup = new ArrayList<>(mProducts);
                adapter = new ProductListAdapter(getActivity(), mProducts, getActivity(), false, null);
                setListAdapter(adapter);
            }
        });
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
            Button submitSearchBtn = bottomSheetDialog.findViewById(R.id.submit_search_products);
            submitSearchBtn.setOnClickListener(f -> {
                EditText productName = bottomSheetDialog.findViewById(R.id.product_name_search);
                EditText productDescription = bottomSheetDialog.findViewById(R.id.description_search);
                EditText minPriceText = bottomSheetDialog.findViewById(R.id.min_price);
                EditText maxPriceText = bottomSheetDialog.findViewById(R.id.max_price);
                EditText productEventType = bottomSheetDialog.findViewById(R.id.event_type_search);
                String name = productName.getText().toString();
                String description = productDescription.getText().toString();
                String eventType = productEventType.getText().toString();
                Integer minPrice = Integer.parseInt(minPriceText.getText().toString().isEmpty() ? "0" : minPriceText.getText().toString());
                Integer maxPrice = Integer.parseInt(maxPriceText.getText().toString().isEmpty() ? "0" : maxPriceText.getText().toString());
                searchProducts(name, description, minPrice, maxPrice, eventType);
                bottomSheetDialog.dismiss();
            });
            Button resetBtn = bottomSheetDialog.findViewById(R.id.reset_search_products);
            resetBtn.setOnClickListener(f -> {
                resetProducts();
                bottomSheetDialog.dismiss();
            });
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

    private void searchProducts(String name, String description, Integer minPrice, Integer maxPrice, String eventType){
        mProducts = new ArrayList<>(mProductsBackup);
        if (!name.isEmpty())
            mProducts.removeIf(p -> !p.getName().toLowerCase().contains(name.toLowerCase()));
        if (!description.isEmpty())
            mProducts.removeIf(p -> !p.getDescription().toLowerCase().contains(description.toLowerCase()));
        if(minPrice != 0 || maxPrice != 0) {
            mProducts.removeIf(p -> minPrice > p.getNewPriceValue());
            mProducts.removeIf(p -> maxPrice < p.getNewPriceValue());
        }
        if(!eventType.isEmpty())
            mProducts.removeIf(p -> !p.containsEventType(eventType));
        adapter = new ProductListAdapter(getActivity(), mProducts, getActivity(), false, null);
        setListAdapter(adapter);
    }
    private void resetProducts(){
        mProducts = new ArrayList<>(mProductsBackup);
        adapter = new ProductListAdapter(getActivity(), mProducts, getActivity(), false, null);
        setListAdapter(adapter);
    }
}
