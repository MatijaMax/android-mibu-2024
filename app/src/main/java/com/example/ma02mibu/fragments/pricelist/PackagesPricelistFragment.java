package com.example.ma02mibu.fragments.pricelist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.PackageListAdapter;
import com.example.ma02mibu.adapters.PackagesPricelistAdapter;
import com.example.ma02mibu.adapters.ServicesPricelistAdapter;
import com.example.ma02mibu.databinding.ProductsPricelistFragmentBinding;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.Owner;
import com.example.ma02mibu.model.Package;
import com.example.ma02mibu.model.Service;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class PackagesPricelistFragment extends ListFragment {
    private ProductsPricelistFragmentBinding binding;
    private ArrayList<Package> mPackages;
    private boolean isOwner = false;
    private String userId;
    private FirebaseAuth auth;
    private PackagesPricelistAdapter adapter;

    public static PackagesPricelistFragment newInstance(){
        PackagesPricelistFragment fragment = new PackagesPricelistFragment();
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
                        adapter = new PackagesPricelistAdapter(getActivity(), mPackages, getActivity(), isOwner);
                        setListAdapter(adapter);
                    }
                    @Override
                    public void onFailure(Exception e) {
                        isOwner = false;
                        getEmployeesPackages();
                        adapter = new PackagesPricelistAdapter(getActivity(), mPackages, getActivity(), isOwner);
                        setListAdapter(adapter);
                    }
                });

            }
        });
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ProductsPricelistFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView header = binding.pricelistHeader;
        String headerText = "Packages pricelist";
        header.setText(headerText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
}
