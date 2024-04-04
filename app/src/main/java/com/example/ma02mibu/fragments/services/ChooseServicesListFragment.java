package com.example.ma02mibu.fragments.services;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.adapters.ProductListAdapter;
import com.example.ma02mibu.adapters.ServiceListAdapter;
import com.example.ma02mibu.databinding.ChooseProductsListBinding;
import com.example.ma02mibu.databinding.ChooseServicesListBinding;
import com.example.ma02mibu.fragments.packages.ChooseProductsListFragment;
import com.example.ma02mibu.fragments.packages.NewPackage;
import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.model.Service;

import java.util.ArrayList;

public class ChooseServicesListFragment extends ListFragment {
    private ChooseServicesListBinding binding;
    private ArrayList<Service> mServices;
    private ArrayList<Service> servicesChosen;
    private ServiceListAdapter adapter;
    private int servicesChosenNum;
    private static final String ARG_PARAM = "param";
    public static ChooseServicesListFragment newInstance(ArrayList<Service> services){
        ChooseServicesListFragment fragment = new ChooseServicesListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, services);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        servicesChosenNum = 0;
        servicesChosen = new ArrayList<>();
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mServices = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new ServiceListAdapter(getActivity(), mServices, getActivity(), true, this);
            setListAdapter(adapter);
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("ShopApp", "onCreateView Products List Fragment");
        binding = ChooseServicesListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button button = binding.submitServicesButton;
    /*    button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(NewPackage.newInstanceFromProducts(productsChosen), getActivity(),
                        true, R.id.scroll_packages_list, "newPackagePage");
            }
        });*/
        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void serviceChosen(Long id){
        servicesChosenNum++;
        TextView textView = binding.servicesChosenNum;
        String s = servicesChosenNum+ " services chosen";
        textView.setText(s);
        Service service = mServices.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
        servicesChosen.add(service);
    }
    public void serviceUnChosen(Long id){
        servicesChosenNum--;
        TextView textView = binding.servicesChosenNum;
        String s = servicesChosenNum+ " products chosen";
        textView.setText(s);
        servicesChosen.removeIf(p -> p.getId() == id);
    }

}
