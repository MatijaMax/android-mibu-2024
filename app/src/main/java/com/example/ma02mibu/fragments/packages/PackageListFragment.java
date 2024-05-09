package com.example.ma02mibu.fragments.packages;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.Owner;
import com.example.ma02mibu.model.Package;
import com.example.ma02mibu.model.Product;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class PackageListFragment extends ListFragment {

    private FragmentPackagesListBinding binding;

    private ArrayList<String> categories;
    private ArrayList<String> subCategories;
    private ArrayList<Package> mPackages;
    private ArrayList<Package> mPackagesBackup;
    private PackageListAdapter adapter;
    private FirebaseAuth auth;
    private String userId;
    private boolean isOwner;
    private static final String ARG_PARAM = "param";
    public static PackageListFragment newInstance(){
        PackageListFragment fragment = new PackageListFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            userId = user.getUid();
        }
        super.onCreate(savedInstanceState);
        CloudStoreUtil.selectPackages(new CloudStoreUtil.PackageCallback(){
            @Override
            public void onCallbackPackage(ArrayList<Package> retrievedPackages) {
                if (retrievedPackages != null) {
                    mPackages = retrievedPackages;
                } else {
                    mPackages = new ArrayList<>();
                }
                CloudStoreUtil.getOwner(userId, new CloudStoreUtil.OwnerCallback() {
                    @Override
                    public void onSuccess(Owner myItem) {
                        isOwner = true;
                        mPackages.removeIf(p -> !p.getOwnerUuid().equals(userId));
                        mPackagesBackup = new ArrayList<>(mPackages);
                        adapter = new PackageListAdapter(getActivity(), mPackages, getActivity(), isOwner);
                        setListAdapter(adapter);
                    }
                    @Override
                    public void onFailure(Exception e) {
                        isOwner = false;
                        getEmployeesPackages();
                        adapter = new PackageListAdapter(getActivity(), mPackages, getActivity(), isOwner);
                        setListAdapter(adapter);
                        binding.newPackageButton.setVisibility(View.GONE);
                    }
                });

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
            Button searchBtn = bottomSheetDialog.findViewById(R.id.search_button_package);
            searchBtn.setOnClickListener(f -> {
                EditText packageName = bottomSheetDialog.findViewById(R.id.package_name_search);
                EditText productName = bottomSheetDialog.findViewById(R.id.product_name_package);
                EditText serviceName = bottomSheetDialog.findViewById(R.id.service_name_package);
                EditText minPriceText = bottomSheetDialog.findViewById(R.id.min_price_package);
                EditText maxPriceText = bottomSheetDialog.findViewById(R.id.max_price_package);
                String packageNameText = packageName.getText().toString();
                String productNameText = productName.getText().toString();
                String serviceNameText = serviceName.getText().toString();
                Integer minPrice = Integer.parseInt(minPriceText.getText().toString().isEmpty() ? "0" : minPriceText.getText().toString());
                Integer maxPrice = Integer.parseInt(maxPriceText.getText().toString().isEmpty() ? "0" : maxPriceText.getText().toString());
                searchPackages(packageNameText, productNameText, serviceNameText, minPrice, maxPrice);
                bottomSheetDialog.dismiss();
            });
            Button resetBtn = bottomSheetDialog.findViewById(R.id.reset_button_package);
            resetBtn.setOnClickListener(f -> {
                resetPackages();
                bottomSheetDialog.dismiss();
            });
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

    private void getEmployeesPackages(){
        CloudStoreUtil.getEmployee(userId, new CloudStoreUtil.EmployeeCallback() {
            @Override
            public void onSuccess(Employee myItem) {
                mPackages.removeIf(s -> !s.getOwnerUuid().equals(myItem.getOwnerRefId()));
            }
            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    private void searchPackages(String packageName, String productName, String serviceName, Integer minPrice, Integer maxPrice){
        mPackages = new ArrayList<>(mPackagesBackup);
        if (!packageName.isEmpty())
            mPackages.removeIf(p -> !p.getName().toLowerCase().contains(packageName.toLowerCase()));
        if (!productName.isEmpty())
            mPackages.removeIf(p -> !p.containsProductName(productName));
        if (!serviceName.isEmpty())
            mPackages.removeIf(p -> !p.containsServiceName(serviceName));
        if(minPrice != 0)
            mPackages.removeIf(p -> minPrice > p.getMinPrice());
        if(maxPrice != 0)
            mPackages.removeIf(p -> maxPrice < p.getMaxPrice());

        adapter = new PackageListAdapter(getActivity(), mPackages, getActivity(), isOwner);
        setListAdapter(adapter);
    }

    private void resetPackages(){
        mPackages = new ArrayList<>(mPackagesBackup);
        adapter = new PackageListAdapter(getActivity(), mPackages, getActivity(), isOwner);
        setListAdapter(adapter);
    }
}
