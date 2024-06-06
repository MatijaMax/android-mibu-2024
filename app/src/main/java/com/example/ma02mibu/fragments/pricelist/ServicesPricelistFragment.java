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
import com.example.ma02mibu.adapters.ProductPricelistAdapter;
import com.example.ma02mibu.adapters.ServiceListAdapter;
import com.example.ma02mibu.adapters.ServicesPricelistAdapter;
import com.example.ma02mibu.databinding.ProductsPricelistFragmentBinding;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.Owner;
import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.model.Service;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ServicesPricelistFragment extends ListFragment {
    private ProductsPricelistFragmentBinding binding;
    private ArrayList<Service> mServices;
    private boolean isOwner = false;
    private String userId;
    private FirebaseAuth auth;
    private ServicesPricelistAdapter adapter;

    public static ServicesPricelistFragment newInstance(){
        ServicesPricelistFragment fragment = new ServicesPricelistFragment();
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
        CloudStoreUtil.selectServices(new CloudStoreUtil.ServiceCallback() {
            @Override
            public void onCallbackService(ArrayList<Service> retrievedServices) {
                if (retrievedServices != null) {
                    mServices = retrievedServices;
                } else {
                    mServices = new ArrayList<>();
                }
                CloudStoreUtil.getOwner(userId, new CloudStoreUtil.OwnerCallback() {
                    @Override
                    public void onSuccess(Owner myItem) {
                        isOwner = true;
                        mServices.removeIf(s -> !s.getOwnerUuid().equals(userId));
                        mServices.removeIf(s -> s.isPending());
                        adapter = new ServicesPricelistAdapter(getActivity(), mServices, getActivity(), isOwner);
                        setListAdapter(adapter);
                    }
                    @Override
                    public void onFailure(Exception e) {
                        isOwner = false;
                        getEmployeesServices();
                        mServices.removeIf(s -> s.isPending());
                        adapter = new ServicesPricelistAdapter(getActivity(), mServices, getActivity(), isOwner);
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
        String headerText = "Services pricelist";
        header.setText(headerText);
        return root;
    }

    private void getEmployeesServices(){
        CloudStoreUtil.getEmployee(userId, new CloudStoreUtil.EmployeeCallback() {
            @Override
            public void onSuccess(Employee myItem) {
                mServices.removeIf(s -> !s.getOwnerUuid().equals(myItem.getOwnerRefId()));
            }
            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
