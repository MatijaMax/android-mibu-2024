package com.example.ma02mibu.fragments.packages;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.PackageListAdapter;
import com.example.ma02mibu.adapters.ProductListAdapter;
import com.example.ma02mibu.databinding.FragmentPackagesListBinding;
import com.example.ma02mibu.model.Package;
import com.example.ma02mibu.model.Product;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class PackageListFragment extends ListFragment {

    private FragmentPackagesListBinding binding;

    private ArrayList<String> categories;
    private ArrayList<String> subCategories;
    private ArrayList<Package> mPackages;
    private PackageListAdapter adapter;
    private static final String ARG_PARAM = "param";
    public static PackageListFragment newInstance(){
        PackageListFragment fragment = new PackageListFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloudStoreUtil.selectPackages(new CloudStoreUtil.PackageCallback(){
            @Override
            public void onCallbackPackage(ArrayList<Package> retrievedPackages) {
                if (retrievedPackages != null) {
                    mPackages = retrievedPackages;
                } else {
                    mPackages = new ArrayList<>();
                }
                adapter = new PackageListAdapter(getActivity(), mPackages, getActivity());
                setListAdapter(adapter);
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("ShopApp", "onCreateView Products List Fragment");
        binding = FragmentPackagesListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button searchButton = binding.searchButtonPackages;
        searchButton.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.FullScreenBottomSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.package_search_dialog, null);
            bottomSheetDialog.setContentView(dialogView);
            Spinner spinnerCategories = bottomSheetDialog.findViewById(R.id.package_category_search);
            Spinner spinnerSubCategories = bottomSheetDialog.findViewById(R.id.package_subcategory_search);
            spinnerCategories.setAdapter(setCategoriesSpinnerAdapter());
            spinnerSubCategories.setAdapter(setSubCategoriesSpinnerAdapter());
            bottomSheetDialog.show();
        });

        Button newPackageButton = binding.newPackageButton;
        newPackageButton.setOnClickListener(v -> {
            FragmentTransition.to(NewPackage.newInstance(), getActivity(),
                    true, R.id.scroll_packages_list, "newPackagePage");
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
